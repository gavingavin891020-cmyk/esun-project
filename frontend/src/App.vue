<template>
  <div class="container">
    <div class="header">
      <img src="https://www.esunbank.com/bank/-/media/esunbank/images/home/logo.png" alt="玉山銀行" class="logo">
      <h1>員工座位安排系統</h1>
    </div>

    <div class="control-panel">
      <label>選擇員工：</label>
      <select v-model="selectedEmpId">
        <option value="">-- 請選擇員工 --</option>
        <option v-for="emp in employees" :key="emp.empId" :value="emp.empId">
          {{ emp.empId }} - {{ emp.name }}
        </option>
      </select>
      <button @click="submitChange" :disabled="!selectedEmpId || !selectedSeatSeq" class="btn-submit">
        送出
      </button>
    </div>

    <div class="seating-area">
      <div 
        v-for="seat in seats" 
        :key="seat.floorSeatSeq"
        :class="['seat-box', getSeatClass(seat)]"
        @click="selectSeat(seat)"
      >
        {{ seat.floorNo }} 樓：座位 {{ seat.seatNo }}
        <div v-if="getOccupiedEmp(seat.floorSeatSeq)" class="emp-info">
          [員編:{{ getOccupiedEmp(seat.floorSeatSeq) }}]
        </div>
      </div>
    </div>

    <div class="legend">
      <span class="legend-item"><i class="box gray"></i> 空位</span>
      <span class="legend-item"><i class="box red"></i> 已佔用</span>
      <span class="legend-item"><i class="box green"></i> 請選擇</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

// 狀態資料
const employees = ref([])
const seats = ref([])
const selectedEmpId = ref('')
const selectedSeatSeq = ref(null)

// 取得資料
const fetchData = async () => {
  try {
    const [empRes, seatRes] = await Promise.all([
      axios.get('http://localhost:8080/api/employees'),
      axios.get('http://localhost:8080/api/seats')
    ])
    employees.value = empRes.data
    seats.value = seatRes.data
  } catch (error) {
    alert('後端連線失敗，請檢查 Spring Boot 是否有啟動並開啟 @CrossOrigin')
  }
}

// 判斷座位顏色邏輯
const getOccupiedEmp = (seq) => {
  const emp = employees.value.find(e => e.floorSeatSeq === seq)
  return emp ? emp.empId : null
}

const getSeatClass = (seat) => {
  if (selectedSeatSeq.value === seat.floorSeatSeq) return 'selected'
  if (getOccupiedEmp(seat.floorSeatSeq)) return 'occupied'
  return 'available'
}

// 點擊座位
const selectSeat = (seat) => {
  if (getOccupiedEmp(seat.floorSeatSeq)) return // 有人坐了不能點
  selectedSeatSeq.value = seat.floorSeatSeq
}

// 送出異動
const submitChange = async () => {
  try {
    await axios.post('http://localhost:8080/api/update-seat', {
     empId: selectedEmpId.value,
      seatSeq: selectedSeatSeq.value
    })
    alert('更新成功！')
    selectedSeatSeq.value = null
    fetchData() // 重新刷新畫面
  } catch (error) {
    alert('更新失敗')
  }
}

onMounted(fetchData)
</script>

<style scoped>
.container { font-family: sans-serif; padding: 20px; max-width: 900px; margin: auto; }
.header { display: flex; align-items: center; border-bottom: 2px solid #00947a; margin-bottom: 20px; }
.logo { height: 40px; margin-right: 15px; }
.control-panel { margin-bottom: 20px; }
.seating-area { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.seat-box { border-radius: 5px; padding: 15px; text-align: center; cursor: pointer; border: 1px solid #ddd; }
.available { background-color: #f0f0f0; }
.occupied { background-color: #e60012; color: white; cursor: not-allowed; }
.selected { background-color: #7dff94; border: 2px solid #00947a; }
.emp-info { font-size: 12px; margin-top: 5px; }
.legend { margin-top: 20px; }
.legend-item { margin-right: 15px; }
.box { display: inline-block; width: 15px; height: 15px; vertical-align: middle; }
.gray { background: #f0f0f0; }
.red { background: #e60012; }
.green { background: #7dff94; }
.btn-submit { margin-left: 20px; padding: 5px 15px; background: #005648; color: white; border: none; cursor: pointer; }
</style>