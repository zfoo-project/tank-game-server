<template>
    <v-footer padless>
        <v-row justify="center" no-gutters>
            <!--            <v-btn-->
            <!--                v-for="link in links"-->
            <!--                :key="link.text"-->
            <!--                :to="link.to"-->
            <!--                text-->
            <!--                rounded-->
            <!--                class="my-2"-->
            <!--            >-->
            <!--                {{ link.text }}-->
            <!--            </v-btn>-->
            <v-col class="text-center" cols="12">
                Copyright&nbsp;{{ new Date().getFullYear() }} — <kbd>zfoo</kbd>
            </v-col>
        </v-row>
    </v-footer>
</template>

<script>
export default {

    name: 'AppFooter',

    data: () => ({
        links: [
            { text: 'Home', to: '/' },
            { text: 'About Us' },
            { text: 'Team' },
            { text: 'Services' },
            { text: 'Contact Us' }
        ]
    })
};
</script>
