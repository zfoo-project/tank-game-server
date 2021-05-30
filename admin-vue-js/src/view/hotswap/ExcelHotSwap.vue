<template>
    <v-card class="mx-auto" max-width="700">

        <v-img v-ripple class="white--text align-end" height="400px" :src="require(`@/asset/excel.jpg`)" />

        <v-card-title>
            {{ $t('excelHotswap') }}
        </v-card-title>

        <v-card-text class="text--primary">
            <div> {{ $t('excelUploadText') }}</div>
            <v-file-input
                v-model="excelFiles"
                chips
                multiple
                show-size
                prepend-icon="mdi-language-java"
                accept=".xlsx"
                color="green lighten-1"
                :label="$t('excelUploadNotice')"
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
import { excelHotswapApi } from '@/apiHttp/hotswapApi.js';

export default {
    name: 'ExcelHotSwap',

    data() {
        return {
            excelFiles: null
        };
    },

    methods: {
        upload() {
            if (_.isEmpty(this.excelFiles)) {
                return;
            }

            const formData = new FormData();
            for (const file of this.excelFiles) {
                formData.append('files', file);
            }

            excelHotswapApi(formData).then(response => {
                this.excelFiles = null;
            });
        }
    }
};
</script>
