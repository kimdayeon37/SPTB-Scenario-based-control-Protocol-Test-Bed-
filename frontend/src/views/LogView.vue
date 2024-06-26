<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { $axios } from '@/axios/index'
import type { QTableProps } from 'quasar'

type LogType = {
  time: string
  type: string
  content: string
}
const selectedDate = ref({ from: '', to: '' })

const records = ref<LogType[]>([])
records.value = [
  {
    time: '2023-09-07T02:58:27.355Z',
    type: '시스템',
    content: '프로그램 시작',
  },
  {
    time: '2023-09-07T02:58:27.355Z',
    type: '시스템',
    content: '프로그램 시작',
  },
  {
    time: '2023-09-07T02:58:27.355Z',
    type: '시스템',
    content: '프로그램 시작uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu',
  },
]

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
  $axios()
    .get('/api/log')
    .then((response) => {
      const data = response.data.data
      for (let i = 0; i < data.length; i++) {
        const item = data[i]
        records.value.push({
          time: item.Time,
          type: item.Type,
          content: item.Content,
        })
      }
    })
    .catch((error) => {
      console.error('Error fetching data:', error)
    })
})

const filter = ref('')
const filteredRecords = computed(() => {
  const fromDate = new Date(selectedDate.value.from)
  const toDate = new Date(selectedDate.value.to)

  return records.value.filter((record) => {
    const recordDate = new Date(record.time)
    return (
      recordDate >= fromDate &&
      recordDate <= toDate &&
      (filter.value.trim() === '' || // 검색 필터가 비어있거나
        record.type.toLowerCase().includes(filter.value.trim().toLowerCase()) ||
        record.content.toLowerCase().includes(filter.value.trim().toLowerCase()))
    ) // 레코드에 검색어가 포함되어 있는 경우
  })
})

const totalValue = computed(() => {
  return filteredRecords.value.length
})
</script>

<template>
  <div class="title flex items-center q-pl-md">
    <div>Log 검색 <strong> > 원하는 날짜 선택</strong></div>
  </div>
  <div class="q-pb-sm">
    <div class="row">
      <div>
        <q-date v-model="selectedDate" range />
      </div>
      <div class="col table-container">
        <q-table flat bordered title="Logs" :rows="filteredRecords" :columns="columns" row-key="time" dense class="table" :pagination="{ rowsPerPage: 15 }">
          <template v-slot:top-right>
            <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
              <template v-slot:append>
                <q-icon name="search" />
              </template>
            </q-input>
          </template>
        </q-table>
      </div>
    </div>
    <q-badge outline color="primary">Total Value: {{ totalValue }}</q-badge>
  </div>
</template>
