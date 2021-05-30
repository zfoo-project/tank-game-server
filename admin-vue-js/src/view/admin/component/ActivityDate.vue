<template>
    <div>
        <v-menu
            v-model="dataMenu"
            :close-on-content-click="false"
            transition="scale-transition"
            offset-y
        >
            <template v-slot:activator="{ on }">
                <v-text-field
                    v-model="date"
                    :label="label"
                    prepend-icon="event"
                    readonly
                    v-on="on"
                />
            </template>
            <v-date-picker
                v-model="date"
                min="0001-01-01"
                max="2222-12-31"
                first-day-of-week="1"
                locale="zh-cn"
                :day-format="(date) => new Date(date).getDate()"
                @input="dataMenu = false"
            />
        </v-menu>
    </div>
</template>

<script>
export default {
    name: 'ActivityDate',
    props: {
        label: {
            type: String,
            default: '',
            required: true
        }
    },
    data() {
        return {
            dataMenu: false,
            date: new Date().toISOString().substr(0, 10)
        };
    },
    methods: {
        setDate(value) {
            this.date = value;
        },

        getDate() {
            return this.date;
        }
    }
};
</script>
