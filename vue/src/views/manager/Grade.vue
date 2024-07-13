<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input v-model="data.courseName" style="width: 240px; margin-right: 10px" placeholder="请输入课程名称查询" :prefix-icon="Search"/>
      <el-input v-model="data.studentName" style="width: 240px; margin-right: 10px" placeholder="请输入学生名称查询" :prefix-icon="Search"/>
            <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>
  </div>

  <div class="card" style="margin-bottom: 10px">
    <div>
      <el-table :data="data.tableData" style="width: 100%">
        <el-table-column prop="courseName" label="课程名称" width="200" />
        <el-table-column prop="studentId" label="学生ID" width="100" v-if="data.user.role === 'ADMIN'"/>
        <el-table-column prop="score" label="分数" />
        <el-table-column prop="comment" label="评语" />
        <el-table-column prop="feedback" label="反馈" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" plain size="small" @click="handleEdit(scope.row)" v-if="data.user.role==='ADMIN'">编辑</el-button>
            <el-button type="danger" plain size="small" @click="del(scope.row.courseId, scope.row.studentId)" v-if="data.user.role==='ADMIN'">删除</el-button>
            <el-button type="primary" plain size="small" @click="handleEdit(scope.row)" v-if="data.user.role==='STUDENT'">反馈</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination :total="data.total" v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     :page-sizes="[10, 20, 50, 100]" :disabled="disabled" :background="background"
                     layout="sizes, prev, pager, next" @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"/>
    </div>
  </div>

  <el-dialog v-model="data.formVisible" title="反馈信息" width="35%" destroy-on-close>
    <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 40px">
      <el-form-item label="评分" v-if="data.user.role==='ADMIN'">
        <el-input v-model="data.form.score" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item label="评语" v-if="data.user.role==='ADMIN'">
        <el-input type="textarea" v-model="data.form.comment" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item label="反馈" v-if="data.user.role==='STUDENT'">
        <el-input type="textarea" v-model="data.form.feedback" autocomplete="off" ></el-input>
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
  courseName: '',
  studentName: '',
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 10,
  user: JSON.parse(localStorage.getItem('student-user') || "{}"),
  Form: {},
  formVisible: false,
})

const load = () => {
  let params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    courseName: data.courseName,
    studentName: data.studentName,
  }
  if(data.user.role === 'STUDENT'){ // 是学生就查询自己的选课记录
    params.studentId = data.user.id
  }
  request.get('/grade/selectPage', {
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
  data.courseName = ''
  data.studentName = ''
  load()
}

const del = (courseId, studentId) => {
  ElMessageBox.confirm("删除分数后需要重新打分，您确定删除吗？", "删除确认", {type: 'warning'}).then(res => {
    request.delete('/grade/delete/studentId=' + studentId + '&courseId=' + courseId).then(res => {
      if(res.code === '200'){
        load()
        ElMessage.success("删除成功")
      }
      else{
        ElMessage.error(res.msg);
      }
    })
  })
}

const handleEdit = (row) => {
  // 弹窗
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true;
}

const save = () => {
  request.put('/grade/update', data.form).then(res => {
    if(res.code === '200'){
      load()
      data.formVisible = false;
      ElMessage.success("保存成功")
    } else{
      ElMessage.error(res.msg);
    }
  })
}

</script>