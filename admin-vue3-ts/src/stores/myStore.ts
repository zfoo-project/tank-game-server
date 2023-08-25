import { defineStore } from "pinia";

interface Snackbar {
  successShow: boolean;
  successMessage: string;
  errorCode: number;
  errorShow: boolean;
  errorMessage: string;
}


export const useSnackbarStore = defineStore({
  id: "myStore",
  state: (): Snackbar => ({
    successShow: false,
    successMessage: "",
    errorCode: 0,
    errorShow: false,
    errorMessage: "",
  }),

  persist: {
    enabled: true,
    strategies: [{ storage: localStorage, paths: ["myStore"] }],
  },

  getters: {},
  actions: {
    setSuccessShow(show: boolean) {
      this.successShow = show;
    },

    setErrorShow(show: boolean) {
      this.errorShow = show;
    },

    showSuccessSnackbar(message: string) {
      this.successShow = true;
      this.successMessage = message;
    },

    showErrorSnackbar(message: string, code: number = 0) {
      this.errorCode = code;
      this.errorShow = true;
      this.errorMessage = message;
    }
  },
});
