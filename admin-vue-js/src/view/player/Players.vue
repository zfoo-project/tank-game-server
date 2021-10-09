<template>
    <v-container>
        <v-row>
            <v-col>
                <v-card-title>
                    {{ $t('player.description') }}
                    <v-spacer />
                    <v-text-field
                        v-model="search"
                        append-icon="search"
                        :label="$t('player.searchContent')"
                        single-line
                        hide-details
                        @click:append="query()"
                        @keyup.enter="query()"
                    />
                </v-card-title>
                <v-data-table
                    :headers="headers"
                    :items="playerRows"
                    :search="search"
                    :loading="loading"
                    :options.sync="options"
                    :server-items-length="totalRows"
                    :footer-props="{itemsPerPageOptions: itemsPerPageOptions}"
                    disable-sort
                />
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import {searchApi} from '@/apiHttp/playerApi.js';

export default {
  name: 'PlayerLevel',
  data: () => ({

    search: '',
    loading: true,
    totalRows: 0,
    options: {
      page: 1,
      itemsPerPage: 20
        },
        headers: [
            { text: '玩家id', value: 'id' },
            { text: '名称', value: 'name' },
            { text: '头像', value: 'avatar' },
            { text: '经验', value: 'exp' },
            { text: '等级', value: 'level' },
            { text: '注册时间', value: 'registerTime' },
            { text: '最近登录时间', value: 'lastLoginTime' }
        ],
        playerRows: [],
        itemsPerPageOptions: [10, 20, 50, 100]
    }),

    watch: {
        options() {
            this.handler();
        }
    },

    created() {
        this.handler();
    },

    methods: {
        handler() {
            this.loading = true;
            searchApi(this.search.trim(), this.options.page, this.options.itemsPerPage).then(response => {
                const data = response.data;
                const page = data.key;
                const result = data.value;
                this.options.page = page.page;
                this.options.itemsPerPage = page.itemsPerPage;
                this.totalRows = page.totalSize;
                this.playerRows = result;
                this.loading = false;
            });
        },


        query() {
            this.options.page = 1;
            this.handler();
        }
    }
};
</script>
