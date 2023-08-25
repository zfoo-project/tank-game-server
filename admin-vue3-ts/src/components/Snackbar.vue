<template>
  <div>
    <v-snackbar v-model="snackbarStore.successShow" timeout="3000" location="top">
      <v-icon color="success">mdi-check-circle-outline</v-icon>
      {{ snackbarStore.successMessage }}
      <template v-slot:actions>
        <v-btn size="small" color="success" icon="mdi-close-circle-outline"
               @click="snackbarStore.setSuccessShow(!snackbarStore.successShow)">
        </v-btn>
      </template>
    </v-snackbar>

    <v-snackbar v-model="snackbarStore.errorShow">
      <v-icon color="error">mdi-alert-circle-outline</v-icon>
      <template v-if="_.isNumber(snackbarStore.errorCode)">
        {{ snackbarStore.errorCode }}:
      </template>
      {{ snackbarStore.errorMessage }}
      <template v-slot:actions>
        <v-btn size="small" color="error" icon="mdi-close-circle-outline"
               @click="snackbarStore.setErrorShow(!snackbarStore.errorShow)">
        </v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script setup lang="ts">
import _ from "lodash";
import {useSnackbarStore} from "@/stores/myStore";
const snackbarStore = useSnackbarStore();
</script>
