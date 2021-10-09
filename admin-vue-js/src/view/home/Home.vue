<template>
    <v-container>
        <v-row>
            <v-col>
                <v-card elevation="6">
                    <v-card-title>
                        <div>
                            <h2 class="title font-weight-regular">{{ $t('home.titleTop') }}</h2>

                            <v-row align="center" class="grey--text text--lighten-1 mx-0 py-3">
                                <v-avatar class="mr-2" size="22">
                                    <v-icon small color="grey lighten-1" @click="() => {}">
                                        mdi-pencil
                                    </v-icon>
                                </v-avatar>

                                <span>{{ $t('home.description') }}</span>
                            </v-row>
                        </div>
                    </v-card-title>

                    <v-img :src="require('@/asset/home.png')" height="700" />
                </v-card>
            </v-col>
        </v-row>

        <app-footer />
    </v-container>
</template>

<script>
export default {
    name: 'Home',

    components: {
        AppFooter: () => import('@/component/base/AppFooter.vue')
    }

};
</script>
