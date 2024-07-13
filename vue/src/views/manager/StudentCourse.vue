<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input v-model="data.name" style="width: 240px" placeholder="请输入课程名称查询" :prefix-icon="Search"/>
      <el-input v-model="data.no" style="width: 240px; margin-right: 10px" placeholder="请输入课程编号查询" :prefix-icon="Search"/>
            <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>
  </div>

  <div class="card" style="margin-bottom: 10px">
    <div>
      <el-table :data="data.tableData" style="width: 100%">
        <el-table-column prop="courseId" label="课程ID" style="width: 100px" />
        <el-table-column prop="studentId" label="学生ID" style="width: 100px" v-if="data.user.role === 'ADMIN'"/>
        <el-table-column prop="name" label="课时名称" />
        <el-table-column prop="no" label="课程编号"  style="width: 100px"/>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="danger" plain size="small" @click="del(scope.row.courseId, scope.row.studentId)">退课</el-button>
            <el-button type="primary" plain size="small" @click="addGrade(scope.row)" v-if="data.user.role==='ADMIN'">打分</el-button>
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

  <el-dialog v-model="data.formVisible" title="成绩信息" width="35%" destroy-on-close>
    <el-form :model="data.gradeForm" label-width="100px" label-position="right" style="padding-right: 40px">
      <el-form-item label="课程编号" style="width: 100px">
        <el-input v-model="data.gradeForm.courseId" autocomplete="off" disabled></el-input>
      </el-form-item>
      <el-form-item label="学生编号" style="width: 100px">
        <el-input v-model="data.gradeForm.studentId" autocomplete="off" disabled></el-input>
      </el-form-item>
      <el-form-item label="课程名称" style="width: 100px">
        <el-input v-model="data.gradeForm.name" autocomplete="off" disabled></el-input>
      </el-form-item>
      <el-form-item label="分数" style="width: 100px">
        <el-input v-model="data.gradeForm.score" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="评语">
        <el-input type="textarea" v-model="data.gradeForm.comment" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<script setup>
import {reactive, ref} from "vue";
import { Search } from '@element-plus/icons-vue';
import request from "../../utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  name: '',
  no: '',
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 10,
  user: JSON.parse(localStorage.getItem('student-user') || "{}"),
  gradeForm: {},
  formVisible: false,
})

const load = () => {
  let params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    name: data.name,
    no: data.no,
  }
  if(data.user.role === 'STUDENT'){ // 是学生就查询自己的选课记录
    params.studentId = data.user.id
  }
  request.get('/studentCourse/selectPage', {
    params: params
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
  load()
}

const del = (courseId, studentId) => {
  ElMessageBox.confirm("退课后需要重新选课，您确定退课吗？", "退课确认", {type: 'warning'}).then(res => {
    request.delete('/studentCourse/delete/studentId=' + studentId + '&courseId=' + courseId).then(res => {
      if(res.code === '200'){
        load()
        ElMessage.success("退课成功")
      }
      else{
        ElMessage.error(res.msg);
      }
    })
  })
}

const addGrade = (row) => {
  // 弹窗
  data.formVisible = true;
  data.gradeForm.name = row.name
  data.gradeForm.courseId = row.courseId
  data.gradeForm.studentId = row.studentId
  data.gradeForm.no = row.no
}

const save = () => {
  request.post('/grade/add', data.gradeForm).then(res => {
    if(res.code === '200'){
      data.formVisible = false
      load()
      ElMessage.success("保存成功")
    } else{
      data.formVisible = false
      ElMessage.error(res.msg);
    }
  })
}

</script>