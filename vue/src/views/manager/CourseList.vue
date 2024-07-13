<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input v-model="data.name" style="width: 240px; margin-right: 10px" placeholder="请输入课程名称查询" :prefix-icon="Search"/>
      <el-input v-model="data.no" style="width: 240px; margin-right: 10px" placeholder="请输入课程编号查询" :prefix-icon="Search"/>
      <el-input v-model="data.teacher" style="width: 240px; margin-right: 10px" placeholder="请输入任课老师查询" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>
  </div>

  <div class="card" style="margin-bottom: 10px">
    <div>
    <el-table :data="data.tableData" style="width: 100%" stripe show-overflow-tooltip height="100%">
      <el-table-column prop="id" label="序号" width="80" sortable/>
      <el-table-column prop="name" label="课程名称" style="width: 200px"/>
      <el-table-column prop="no" label="课程编号" style="width: 200px"/>
      <el-table-column prop="descr" label="课程描述" />
      <el-table-column prop="times" label="课时" style="width: 200px"/>
      <el-table-column prop="teacher" label="任课老师" style="width: 200px"/>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary" plain size="small" @click="selectCourse(scope.row)">选课</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <div class="card">
    <el-pagination :total="data.total" v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                   :page-sizes="[10, 20, 50, 100]" :disabled="disabled" :background="background"
                   layout="sizes, prev, pager, next" @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"/>
  </div></div>

</template>

<script setup>
import {reactive, ref} from "vue";
import { Search } from '@element-plus/icons-vue';
import request from "../../utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  name: '',
  no: '',
  teacher: '',
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 10,
  user: JSON.parse(localStorage.getItem('student-user') || "{}")
})

const load = () => {
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      no: data.no,
      teacher: data.teacher,
    }
  }).then(res => {
    data.tableData = res.data?.list || []   // 没有data时不报错，加个问号
    data.total = res.data?.total || 0
  })
}

const background = ref(false)
const disabled = ref(false)


// 调用方法获取后台数据
load();

const handleCurrentChange = () => {
  // 重新加载数据即可
  load()
}

const handleSizeChange = () => {
  load()
}

const reset = () => {
  data.name = ''
  data.no = ''
  data.teacher = ''
  load()
}

const selectCourse = (row) => {
  request.post('studentCourse/add', {studentId: data.user.id, name: row.name, no: row.no, courseId: row.id, username: data.user.username}).then(res => {
    if(res.code === '200'){
      ElMessage.success("操作成功")
    } else{
      ElMessage.error(res.msg);
    }
  })
}

</script>