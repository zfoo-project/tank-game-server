<template>
  <v-card class="pa-3" elevation="3">
    <v-card-title primary-title class="my-4 text-h4"> Welcome</v-card-title>
    <v-card-subtitle>Sign in to your account</v-card-subtitle>
    <!-- sign in form -->

    <v-card-text>
      <v-form
        ref="refLoginForm"
        class="text-left"
        lazy-validation
      >
        <v-text-field
          ref="refEmail"
          v-model="userName"
          required
          :error="error"
          :label="$t('user.account')"
          :density="formStyle.density"
          :variant="formStyle.variant"
          :color="formStyle.borderColor"
          :bg-color="formStyle.bgColor"
          name="email"
          outlined
          validateOn="blur"
          @keyup.enter="submit"
          @change="resetErrors"
        ></v-text-field>
        <v-text-field
          ref="refPassword"
          v-model="password"
          :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
          :type="showPassword ? 'text' : 'password'"
          :error="error"
          :error-messages="errorMessages"
          :label="$t('user.password')"
          :density="formStyle.density"
          :variant="formStyle.variant"
          :color="formStyle.borderColor"
          :bg-color="formStyle.bgColor"
          name="password"
          outlined
          validateOn="blur"
          @change="resetErrors"
          @keyup.enter="submit"
          @click:append-inner="showPassword = !showPassword"
        ></v-text-field>
        <v-btn
          :loading="isLoading"
          :disabled="isSignInDisabled"
          block
          size="x-large"
          color="primary"
          @click="submit"
          class="mt-2"
        >{{ $t("login.button") }}
        </v-btn
        >

        <div
          class="text-grey text-center text-caption font-weight-bold text-uppercase my-5"
        >
          {{ $t("login.orsign") }}
        </div>

        <!-- external providers list -->
        <v-btn
          class="mb-2 lighten-2 text-capitalize"
          block
          size="x-large"
          to="/"
          :disabled="isSignInDisabled"
        >
          <Icon icon="logos:google-icon" class="mr-3 my-2"/>
          Google
        </v-btn>
        <v-btn
          class="mb-2 lighten-2 text-capitalize"
          block
          size="x-large"
          to="/"
          :disabled="isSignInDisabled"
        >
          <Icon icon="logos:facebook" class="mr-3"/>
          Facebook
        </v-btn>

        <div v-if="errorProvider" class="error--text my-2">
          {{ errorProviderMessages }}
        </div>

        <div class="mt-5 text-center">
          <router-link class="text-primary" to="/auth/forgot-password">
            {{ $t("login.forgot") }}
          </router-link>
        </div>
      </v-form>
    </v-card-text
    >
  </v-card>
  <div class="text-center mt-6">
    {{ $t("login.noaccount") }}
    <router-link to="/auth/signup" class="text-primary font-weight-bold">
      {{ $t("login.create") }}
    </router-link>
  </div>
</template>
<script setup lang="ts">
import {Icon} from "@iconify/vue";
import {useRouter} from "vue-router";
import axios, {AxiosResponse} from "axios";
import {useI18n} from 'vue-i18n'

const {t} = useI18n()
import {useSnackbarStore} from "@/stores/myStore";

const snackbarStore = useSnackbarStore();
import {BASE_URL, setToken} from "@/utils/authUtils";
import _ from "lodash";

const router = useRouter();
//
const refLoginForm = ref(null);
const refEmail = ref(null);
const refPassword = ref(null);

// sign in buttons
const isLoading = ref(false);
const isSignInDisabled = ref(false);


const userName = ref("");
const password = ref("");

const error = ref(false);
const errorMessages = ref("");

const errorProvider = ref(false);
const errorProviderMessages = ref("");

// show password field
const showPassword = ref(false);

const formStyle = reactive({
  labelColor: "3B64A0",
  mainColor: "#3F69F4",
  borderColor: "#42a5f5",
  bgColor: "#fff",
  density: "default", //	'default' | 'comfortable' | 'compact'
  variant: "underlined", // 'flat' | 'elevated' | 'tonal' | 'outlined' | 'text' | 'plain'
});

const rules = reactive({
  required: (value) => (value && Boolean(value)) || "Required",
});


const submit = async () => {
  signIn(userName.value, password.value);
};

const signIn = async (userName, password) => {
  if (_.isEmpty(userName)) {
    snackbarStore.showErrorSnackbar(t('notice.loginNameEmptyError'));
    return;
  }

  if (_.isEmpty(password)) {
    snackbarStore.showErrorSnackbar(t('notice.loginPasswordEmptyError'));
    return;
  }

  isLoading.value = true;
  isSignInDisabled.value = true;

  const response = await axios.post(BASE_URL + "/api/signIn", {
    userName,
    password
  }).catch(reason => {
    snackbarStore.showErrorSnackbar(reason);
  }).finally(() => {
    isLoading.value = false;
    isSignInDisabled.value = false;
  });

  const code = response.data.code;
  if (code != 1) {
    snackbarStore.showErrorSnackbar(response.data.message, code);
    return;
  }

  snackbarStore.showSuccessSnackbar(response.data.message);
  isLoading.value = false;
  isSignInDisabled.value = false;

  console.log(response);
  setToken(response.data.data.token);
  console.log("************************************************");
  router.push({path: "/",});
};


const resetErrors = () => {
  error.value = false;
  errorMessages.value = "";
};
</script>
