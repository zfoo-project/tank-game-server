<template>
    <v-card class="mx-auto" max-width="700">
        <v-img class="white--text align-end" height="400px" :src="require(`@/asset/level.jpg`)" />

        <v-card-title> {{ $t('gm.level') }}</v-card-title>

        <v-card-text class="text--primary">
            <v-text-field v-model="userName" :label=" $t('player.searchContent')" prepend-icon="mdi-car-convertible" type="text" color="green lighten-1" />

            <v-text-field v-model="playerLevel" :label="$t('gm.levelInput')" prepend-icon="mdi-keyboard-f1" type="number" color="green lighten-1" />

            <v-text-field v-model="exp" :label="$t('gm.expInput')" prepend-icon="mdi-keyboard-f2" type="number" color="green lighten-1" />
        </v-card-text>

        <v-card-actions>
            <v-spacer />
            <v-btn color="primary" @click="confirm()">
                {{ $t('common.confirm') }}
            </v-btn>
        </v-card-actions>

    </v-card>
</template>

<script>
import { playerLevelApi } from '@/apiHttp/gmApi.js';

export default {
    name: 'PlayerLevel',
    data: () => ({
        userName: '',
        playerLevel: null,
        exp: null
    }),
    mounted() {

    },
    methods: {
        confirm() {
            const playerLevel = this.playerLevel === null ? -1 : this.playerLevel;
            const exp = this.exp === null ? -1 : this.exp;
            playerLevelApi(this.userName, playerLevel, exp);
        }
    }
};
</script>
