<script setup lang="ts">
import { ref } from 'vue'
import type { NetworkMasterData } from '../../types'
import MMENetworkDialog from './MME-NetworkDialog.vue'
import { useToggleStore } from '../../store/modules/settingtoggle'

const props = defineProps<{
  networkData: NetworkMasterData
  viewLogToggle: boolean
}>()
const emits = defineEmits<{
  setNetworkMasterData: [data: NetworkMasterData]
  setViewLogToggle: [bool: boolean]
  'update:viewLogToggle': [bool: boolean]
}>()

const networkDialogToggle = ref<boolean>(true)
const setNetworkDialogToggle = (bool?: boolean) => {
  if (bool) networkDialogToggle.value = bool
  else networkDialogToggle.value = !networkDialogToggle.value
}

const toggleStore = useToggleStore()
//toggleStore.toggleSetting(false)
toggleStore.networkDialogToggle = false
toggleStore.toggle()
const setNetworkMasterData = (data: NetworkMasterData) => {
  emits('setNetworkMasterData', data)
}
</script>
<template>
  <div class="topbar-container">
    <div class="column">
      <div class="title flex items-center q-pl-md">
        <div>Modbus > <strong>Master Ethernet</strong></div>
      </div>
      <div class="menu-bar row items-center">
        <q-btn :outline="!toggleStore.networkDialogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="toggleStore.toggle()">
          통신 설정
        </q-btn>
        <q-btn :outline="props.viewLogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="emits('update:viewLogToggle', false)">
          메세지
        </q-btn>
        <q-btn :outline="!props.viewLogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="emits('update:viewLogToggle', true)">
          로그
        </q-btn>
      </div>
      <MMENetworkDialog
        @setNetworkMasterData="setNetworkMasterData"
        @setNetworkDialogToggle="setNetworkDialogToggle"
        :networkDialogToggle="networkDialogToggle"
        :networkData="networkData"
      />
    </div>
  </div>
</template>
<style scoped>
.title {
  height: 40px;
  border-bottom: solid 1px;
  border-color: #bcbcbc;
  background: #f3f4f5;
}
</style>
