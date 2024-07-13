<template>
<!-- 增查操作 -->
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input v-model="data.name" style="width: 240px; margin-right: 10px" placeholder="请输入课程名称查询" :prefix-icon="Search"/>
      <el-input v-model="data.no" style="width: 240px; margin-right: 10px" placeholder="请输入课程编号查询" :prefix-icon="Search"/>
      <el-input v-model="data.teacher" style="width: 240px; margin-right: 10px" placeholder="请输入任课老师查询" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
      <el-button type="primary" @click="handleAdd" style="margin-left: 10px">新增</el-button>
    </div>
  </div>
<!-- 表格数据，删除修改操作 -->
  <div>
    <el-table :data="data.tableData" style="width: 100%" stripe show-overflow-tooltip height="100%">
      <el-table-column prop="id" label="序号" width="80" sortable/>
      <el-table-column prop="name" label="课时名称" />
      <el-table-column prop="no" label="课程编号" />
      <el-table-column prop="descr" label="课程描述" />
      <el-table-column prop="times" label="课时" />
      <el-table-column prop="teacher" label="任课老师" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary" size="small" plain @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" plain @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
<!-- 分页功能 -->
  <div class="card">
      <el-pagination :total="data.total" v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     :page-sizes="[10, 20, 50, 100]" :disabled="disabled" :background="background"
                     layout="sizes, prev, pager, next" @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"/>
  </div>
<!--弹窗-->
  <el-dialog v-model="data.formVisible" title="课程信息" width="35%">
    <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 30px">
      <el-form-item label="课程名称">
        <el-input v-model="data.form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="课程编号">
        <el-input v-model="data.form.no" autocomplete="off" />
      </el-form-item>
      <el-form-item label="课程描述">
        <el-input v-model="data.form.descr" autocomplete="off" />
      </el-form-item>
      <el-form-item label="课时">
        <el-input v-model="data.form.times" autocomplete="off" />
      </el-form-item>
      <el-form-item label="任课老师">
        <el-input v-model="data.form.teacher" autocomplete="off" />
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
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 10,
  no: '',
  teacher: '',
  formVisible: false,
  form: {}
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

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const save = () => {
  request.request({
    url: data.form.id ? '/course/update' : '/course/add',
    method: data.form.id ? 'PUT' : 'POST',
    data: data.form
  }).then(res => {
    if(res.code === '200'){
      // 重新获取数据
      load();
      data.formVisible = false
      ElMessage.success("操作成功")
    } else{
      ElMessage.error(res.msg);
    }
  })
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const handleDelete = (id) => {
  ElMessageBox.confirm("删除数据后无法恢复，您确认删除吗？", "删除确认", { type: 'warning'}).then(res => {
    request.delete('/course/delete/' + id).then(res => {
      if(res.code === '200'){
        // 重新获取数据
        load();
        data.formVisible = false
        ElMessage.success("操作成功")
      } else{
        ElMessage.error(res.msg);
      }
    })
  })
}
</script>