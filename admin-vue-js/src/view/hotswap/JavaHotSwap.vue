<template>
    <v-card class="mx-auto" max-width="700">

        <v-img v-ripple class="white--text align-end" height="400px" :src="require(`@/asset/java.jpg`)" />

        <v-card-title>
            {{ $t('javaHotswap') }}
        </v-card-title>

        <v-card-text class="text--primary">
            <div> {{ $t('javaUploadText') }}</div>
            <v-file-input
                v-model="classFiles"
                chips
                multiple
                show-size
                prepend-icon="mdi-language-java"
                accept=".class"
                color="green lighten-1"
                :label="$t('javaUploadNotice')"
            />
        </v-card-text>

        <v-card-actions>
            <v-spacer />
            <v-btn color="primary" @click="upload()">
                {{ $t('upload') }}
            </v-btn>
        </v-card-actions>

    </v-card>
</template>

<script>
import { javaHotswapApi } from '@/apiHttp/hotswapApi.js';

export default {
    name: 'JavaHotSwap',

    data() {
        return {
            classFiles: null
        };
    },

    methods: {
        upload() {
            if (_.isEmpty(this.classFiles)) {
                return;
            }

            const formData = new FormData();
            for (const file of this.classFiles) {
                formData.append('files', file);
            }

            javaHotswapApi(formData).then(response => {
                this.classFiles = null;
            });
        }
    }
};
</script>
