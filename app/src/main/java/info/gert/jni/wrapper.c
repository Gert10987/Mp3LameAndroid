#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <android/log.h>
#include "libmp3lame/lame.h"

#define LOG_TAG "LAME ENCODER"
#define LOGD(format, args...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, format, ##args);
#define BUFFER_SIZE 8192
#define be_short(s) ((short) ((unsigned short) (s) << 8) | ((unsigned short) (s) >> 8))

lame_t lame;

int read_samples(FILE *input_file, short *input) {
	int nb_read;
	nb_read = fread(input, 1, sizeof(short), input_file) / sizeof(short);

	int i = 0;
	while (i < nb_read) {
		input[i] = be_short(input[i]);
		i++;
	}

	return nb_read;
}

void Java_info_gert_mp3lameAndroid_Mp3Encoder_initEncoder(JNIEnv *env,
		jobject jobj, jint in_num_channels, jint in_samplerate, jint in_brate,
		jint in_mode, jint in_quality) {
	lame = lame_init();

//	LOGD("Init parameters:");
	lame_set_num_channels(lame, in_num_channels);
//	LOGD("Number of channels: %d", in_num_channels);
	lame_set_in_samplerate(lame, in_samplerate);
//	LOGD("Sample rate: %d", in_samplerate);
	lame_set_brate(lame, in_brate);
//	LOGD("Bitrate: %d", in_brate);
	lame_set_mode(lame, in_mode);
//	LOGD("Mode: %d", in_mode);
	lame_set_quality(lame, in_quality);
//	LOGD("Quality: %d", in_quality);

	int res = lame_init_params(lame);
//	LOGD("Init returned: %d", res);
}

void Java_info_gert_mp3lameAndroid_Mp3Encoder_destroyEncoder(
		JNIEnv *env, jobject jobj) {
	int res = lame_close(lame);
//	LOGD("Deinit returned: %d", res);
}

int Java_info_gert_mp3lameAndroid_Mp3Encoder_encode(JNIEnv *env, jobject jobj, jstring in_source_path, jstring in_target_path){

const char *source_path, *target_path;
source_path = (*env)->GetStringUTFChars(env, in_source_path, NULL);
target_path = (*env)->GetStringUTFChars(env, in_target_path, NULL);

int read, write;

    FILE *pcm = fopen(source_path, "rb");
    FILE *mp3 = fopen(target_path, "wb");

    const int PCM_SIZE = 8192;
    const int MP3_SIZE = 8192;

    short int pcm_buffer[PCM_SIZE*2];
    unsigned char mp3_buffer[MP3_SIZE];

    lame_t lame = lame_init();
    lame_set_in_samplerate(lame, 44100);
    lame_set_VBR(lame, vbr_off);
    lame_init_params(lame);

    do {
        read = fread(pcm_buffer, 2*sizeof(short int), PCM_SIZE, pcm);
        if (read == 0)
            write = lame_encode_flush(lame, mp3_buffer, MP3_SIZE);
        else
            write = lame_encode_buffer_interleaved(lame, pcm_buffer, read, mp3_buffer, MP3_SIZE);
        fwrite(mp3_buffer, write, 1, mp3);
    } while (read != 0);

    lame_close(lame);
    fclose(mp3);
    fclose(pcm);

    return 0;
}

