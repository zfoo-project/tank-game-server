<template>
  <v-card class="mx-auto" max-width="700">
    <img class="white--text align-end" src="@/assets/tank/currency.jpg"/>
    <v-card-title>{{ $t('gm.currency') }}</v-card-title>

    <v-card-text class="text--primary">
      <v-text-field v-model="userName" :label="$t('player.searchContent')" prepend-icon="mdi-car-convertible"
                    type="text"/>

      <v-text-field v-model="gold" :label="$t('gm.goldInput')" prepend-icon="mdi-keyboard-f1" type="number"/>
      <v-text-field v-model="gem" :label="$t('gm.gemInput')" prepend-icon="mdi-keyboard-f2" type="number"/>
      <v-text-field v-model="energy" :label="$t('gm.energyInput')" prepend-icon="mdi-keyboard-f3" type="number"/>
    </v-card-text>

    <v-card-actions>
      <v-spacer/>
      <v-btn color="primary" variant="flat" @click="confirm()">
        {{ $t('common.confirm') }}
      </v-btn>
    </v-card-actions>

  </v-card>
</template>

<script setup lang="ts">
import axios, {AxiosResponse} from "axios";
import {BASE_URL, httpHeaders} from "@/utils/authUtils";
import {useSnackbarStore} from "@/stores/myStore";

const snackbarStore = useSnackbarStore();

const route = useRoute();

const userName = ref("");
const gold = ref(null);
const gem = ref(null);
const energy = ref(null);

const confirm = async () => {
  const goldValue = gold.value === null ? -1 : gold.value;
  const gemValue = gem.value === null ? -1 : gem.value;
  const energyValue = energy.value === null ? -1 : energy.value;

  const response = await axios.post(BASE_URL + "/api/gm/currency", {
    "userName": userName.value,
    "gold": goldValue,
    "gem": gemValue,
    "energy": energyValue
  }, httpHeaders());

  const code = response.data.code;
  if (code != 1) {
    snackbarStore.showErrorSnackbar(response.data.message, code);
    return;
  }
  snackbarStore.showSuccessSnackbar(response.data.message);
}

</script>
