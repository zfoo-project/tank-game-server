<template>
    <div>
        <v-snackbar v-model="successShow" :timeout="timeout" top>
            <v-icon color="success">mdi-check-circle-outline</v-icon>

            {{ successMessage }}

            <template v-slot:action="{ attrs }">
                <v-btn fab small color="success" v-bind="attrs" @click="successShow = !successShow">
                    <v-icon>mdi-close-circle-outline</v-icon>
                </v-btn>
            </template>
        </v-snackbar>

        <v-snackbar v-model="errorShow" :timeout="timeout">
            <v-icon color="error">mdi-alert-circle-outline</v-icon>

            <template v-if="_.isNumber(errorCode)">
                {{ errorCode }}:
            </template>
            {{ errorMessage }}

            <template v-slot:action="{ attrs }">
                <v-btn fab small color="error" v-bind="attrs" @click="errorShow = !errorShow">
                    <v-icon>mdi-close-circle-outline</v-icon>
                </v-btn>
            </template>
        </v-snackbar>
    </div>
</template>

<script>

export default {
    name: 'Snackbar',
    data: () => ({
        successShow: false,
        successMessage: '',

        errorCode: '',
        errorShow: false,
        errorMessage: '',

        timeout: 3000
    }),

    methods: {
        showSuccessSnackbar(message) {
            this.successShow = true;
            this.successMessage = message;
        },

        showErrorSnackbar(message, code) {
            this.errorShow = true;
            this.errorMessage = message;
            this.errorCode = code;
        }
    }
};
</script>
