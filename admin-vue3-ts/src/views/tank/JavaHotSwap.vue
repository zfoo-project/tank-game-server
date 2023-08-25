<template>
  <v-card class="mx-auto" max-width="700">

    <img class="white--text align-end" src="@/assets/tank/java.jpg"/>

    <v-card-title>
      {{ $t("javaHotswap") }}
    </v-card-title>

    <v-card-text>
      <div> {{ $t("javaUploadText") }}</div>
      <v-file-input
        v-model="javaRef.javaFiles"
        multiple
        show-size
        prepend-icon="mdi-language-java"
        accept=".class"
        :label="$t('javaUploadNotice')"
      />
    </v-card-text>

    <v-card-actions>
      <v-spacer/>
      <v-btn color="primary" variant="flat" @click="upload()">
        {{ $t("upload") }}
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

interface JavaFiles {
  javaFiles: File[] | null
}

let excelFilesObj: JavaFiles = {
  javaFiles: null
}

const javaRef = reactive(excelFilesObj);

const upload = async () => {
  if (javaRef.javaFiles === null) {
    return;
  }

  const formData = new FormData();
  for (const file of javaRef.javaFiles) {
    formData.append("files", file);
  }
  const response = await axios.post(BASE_URL + "/api/java/hotswap", formData, httpHeaders());

  const code = response.data.code;
  if (code != 1) {
    snackbarStore.showErrorSnackbar(response.data.message, code);
    return;
  }
  snackbarStore.showSuccessSnackbar(response.data.message);
}

</script>
