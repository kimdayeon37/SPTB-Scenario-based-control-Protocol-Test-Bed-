<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { QTableProps } from 'quasar'
import { useIdStore } from '../store/idStore'
import { useUserStore } from '../store/userStore'
const userStore = useUserStore()
const idStore = useIdStore()
type LogType = {
  time: string
  type: string
  content: string
}
const logs = ref<LogType[]>([])
logs.value = []
const columns: QTableProps['columns'] = [
  {
    name: 'time',
    label: '시간',
    field: 'time',
    sortable: true,
    align: 'left',
    headerStyle: 'width: 100px',
  },
  {
    name: 'type',
    required: true,
    label: '구분',
    align: 'left',
    sortable: true,
    field: 'type',
    headerStyle: 'width: 100px',
  },
  {
    name: 'content',
    required: true,
    label: '내용',
    align: 'left',
    field: 'content',
    sortable: true,
  },
]

onMounted(() => {
  const clientId = idStore.clientId
  const eventSource = new EventSource('/api/sse/trans?authorization=Bearer ' + userStore.token + '&clientId=' + clientId) // 서버 SSE 엔드포인트 주소

  eventSource.addEventListener('message', (event) => {
    const data = JSON.parse(event.data)
    logs.value.unshift({
      time: data.time,
      type: data.type,
      content: data.content,
    })
  })
})
</script>
<template>
  <div class="col table-container">
    <q-table flat square :rows="logs" :columns="columns" row-key="time" dense class="table" :rows-per-page-options="[0]" hide-no-data hide-pagination />
  </div>
</template>
<style></style>
