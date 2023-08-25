<template>
  <v-container>
    <v-row>
      <v-col>
        <v-card-title>
          {{ $t('player.description') }}
          <v-spacer/>
          <v-text-field
            v-model="players.search"
            append-icon="search"
            :label="$t('player.searchContent')"
            single-line
            hide-details
            @click:append="query()"
            @keyup.enter="query()"
          />
        </v-card-title>
        <v-data-table-server
          :headers="players.headers"
          :items="players.playerRows"
          :search="players.search"
          :loading="players.loading"
          :items-per-page="players.options.itemsPerPage"
          :items-length="players.totalRows"
          @update:options="onUpdateOptions"
          @update:page="onUpdatePage"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import axios from "axios";
import {BASE_URL, httpHeaders} from "@/utils/authUtils";
import {useSnackbarStore} from "@/stores/myStore";
import {onMounted} from "vue";
import _ from "lodash";

const snackbarStore = useSnackbarStore();
const route = useRoute();


const players = reactive({
  search: '',
  loading: true,
  totalRows: 0,
  options: {
    page: 1,
    itemsPerPage: 10
  },
  headers: [
    {title: '玩家id', key: 'id'},
    {title: '名称', key: 'name'},
    {title: '头像', key: 'avatar'},
    {title: '经验', key: 'exp'},
    {title: '等级', key: 'level'},
    {title: '注册时间', key: 'registerTime'},
    {title: '最近登录时间', key: 'lastLoginTime'}
  ],
  playerRows: []
});


const fetchData = async () => {
  players.loading = true;

  const query = players.search.trim();
  const page = players.options.page;
  const itemsPerPage = players.options.itemsPerPage;
  console.log("------------------" + page)
  const response = await axios.post(BASE_URL + "/api/player/search", {
    query,
    page,
    itemsPerPage
  }, httpHeaders());

  const code = response.data.code;
  if (code != 1) {
    snackbarStore.showErrorSnackbar(response.data.message, code);
    return;
  }

  const data = response.data.data;
  console.log(data);
  const serverPage = data.key;
  const result = data.value;
  players.options = {
    page: serverPage.page,
    itemsPerPage: serverPage.itemsPerPage,
  };
  players.totalRows = serverPage.totalSize
  players.playerRows = result;
  players.loading = false;
  snackbarStore.showSuccessSnackbar(response.data.message);
}

async function onUpdateOptions(options) {
  players.options = options
  await fetchData()
}

function onUpdatePage(page) {
  players.options.page = page
}

const query = async () => {
  players.options.page = 1;
  fetchData();
}

onMounted(() => {
  fetchData();
});

watch(
  () => players.options,
  (newValue, oldValue) => {
    // console.log("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww")
    // console.log(newValue)
    // console.log(oldValue)
    if (_.isEqual(newValue, oldValue)) {
      return;
    }
    // console.log("----------------------------------------------------------")
    fetchData();
  }
)


</script>
