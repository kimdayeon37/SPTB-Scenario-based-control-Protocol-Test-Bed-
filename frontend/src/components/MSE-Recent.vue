<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useToggleStore } from '../store/modules/settingtoggle'
import { useRouter } from 'vue-router'

type SettingType = {
  protocol?: string
  port?: number
  maxSessionCount?: number
  slaveId?: number
  waitTimeout?: number
}

const router = useRouter()
const openCard = (selectedData: SettingType) => {
  const toggleStore = useToggleStore()
  //toggleStore.toggleSetting(false)
  toggleStore.networkDialogToggle = false
  toggleStore.toggle()

  // 선택한 데이터를 JSON 문자열로 변환하여 URL params로 전달
  const selectedDataJSON = JSON.stringify(selectedData)
  router.push({ name: 'SlaveEthernet', query: { selectedData: selectedDataJSON } })
}

const deleteCard = (index: number) => {
  savedData.value.splice(index, 1)

  // Remove the corresponding local storage value
  const savedDataJSON = localStorage.getItem('slaveEthernetData')
  if (savedDataJSON) {
    const parsedData = JSON.parse(savedDataJSON) as SettingType[]
    parsedData.splice(index, 1)
    localStorage.setItem('slaveEthernetData', JSON.stringify(parsedData))
  }
}

let savedData = ref<(SettingType & { ratingModel: number })[]>([])

// 데이터 로드 함수
const loadData = () => {
  try {
    const savedDataJSON = localStorage.getItem('slaveEthernetData')
    if (savedDataJSON) {
      savedData.value = JSON.parse(savedDataJSON)
    }
  } catch (error) {
    console.error('Error loading data from localStorage:', error)
  }
}
// 페이지가 로드될 때 데이터 로드 실행
onMounted(() => {
  loadData()
})

let buttonActivated = ref(true)

const moveToTop = (index: number) => {
  if (buttonActivated.value && savedData.value) {
    // Remove the clicked card session from its current position
    const clickedData = savedData.value.splice(index, 1)[0]

    // Insert the clicked card session at the beginning
    savedData.value.unshift(clickedData)
    localStorage.setItem('slaveEthernetData', JSON.stringify(savedData.value))
  }
}
</script>

<template>
  <div v-for="(data, i) in savedData" :key="i">
    <q-card class="my-card bg-primary text-white">
      <q-card-section>
        <div class="text-h6">{{ data.protocol }}</div>
        <div class="text-subtitle2">[Port] {{ data.port }}</div>
        <div class="text-subtitle2">[MaxSessionCount] {{ data.maxSessionCount }}</div>
        <div class="text-subtitle2">[SlaveId] {{ data.slaveId }}</div>
        <div class="text-subtitle2">[WaitTimeout] {{ data.waitTimeout }}</div>
      </q-card-section>

      <q-separator dark />

      <q-card-actions>
        <q-btn flat @click="deleteCard(i)">Delete</q-btn>
        <q-btn flat @click="() => openCard(data)">Open</q-btn>

        <q-btn flat @click="moveToTop(i)">
          <q-rating v-model="data.ratingModel" size="1em" :max="1" color="yellow" class="rating">
            <template v-slot:tip-1>
              <q-tooltip>고정!</q-tooltip>
            </template>
          </q-rating>
        </q-btn>
      </q-card-actions>
    </q-card>
  </div>
</template>
