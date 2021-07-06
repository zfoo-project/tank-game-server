<template>
    <v-card class="mx-auto" max-width="700">
        <v-img class="white--text align-end" height="400px" :src="require(`@/asset/currency.jpg`)" />

        <v-card-title>{{ $t('gm.currency') }}</v-card-title>

        <v-card-text class="text--primary">
            <v-text-field v-model="userName" :label="$t('player.searchContent')" prepend-icon="mdi-car-convertible" type="text" color="green lighten-1" />

            <v-text-field v-model="gold" :label="$t('gm.goldInput')" prepend-icon="mdi-keyboard-f1" type="number" color="green lighten-1" />
            <v-text-field v-model="gem" :label="$t('gm.gemInput')" prepend-icon="mdi-keyboard-f2" type="number" color="green lighten-1" />
            <v-text-field v-model="energy" :label="$t('gm.energyInput')" prepend-icon="mdi-keyboard-f3" type="number" color="green lighten-1" />
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
import { currencyApi } from '@/apiHttp/gmApi.js';

export default {
    name: 'Currency',
    data: () => ({
        userName: '',
        gold: null,
        gem: null,
        energy: null
    }),

    methods: {
        confirm() {
            const gold = this.gold === null ? -1 : this.gold;
            const gem = this.gem === null ? -1 : this.gem;
            const energy = this.energy === null ? -1 : this.energy;

            currencyApi(this.userName, gold, gem, energy);
        }
    }
};
</script>
