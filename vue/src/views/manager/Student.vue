<template>
<!--查询和新增操作-->
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input v-model="data.username" style="width: 240px; margin-right: 10px" placeholder="请输入账号查询" :prefix-icon="Search"/>
      <el-input v-model="data.name" style="width: 240px; margin-right: 10px" placeholder="请输入学生名称查询" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
      <el-button type="primary" @click="handleAdd" style="margin-left: 10px">新增</el-button>
    </div>
  </div>

<!--学生信息展示-->
  <div>
    <el-table :data="data.tableData" style="width: 100%" stripe show-overflow-tooltip height="100%">
      <el-table-column prop="id" label="序号" width="80" sortable/>
      <el-table-column prop="username" label="学生账号" />
      <el-table-column prop="name" label="学生名称" />
      <el-table-column prop="phone" label="学生手机号" />
      <el-table-column prop="email" label="学生邮箱" width="200"/>
      <el-table-column prop="sex" label="性别" width="70"/>
      <el-table-column prop="birth" label="生日" />
      <el-table-column prop="address" label="地址" />
      <el-table-column prop="avatar" label="头像">
        <template #default="scope">
          <el-image v-if="scope.row.avatar" :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" style="width: 40px; height: 40px; border-radius: 5px"></el-image>
        </template>
        </el-table-column>
      <el-table-column width="180" label="操作" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" plain @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" plain @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

<!-- 页码显示 -->
  <div class="card">
      <el-pagination :total="data.total" v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     :page-sizes="[10, 20, 50, 100]" :disabled="disabled" :background="background"
                     layout="sizes, prev, pager, next" @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"/>
  </div>

<!--新增操作的弹窗-->
  <el-dialog v-model="data.formVisible" title="学生信息" width="35%" destroy-on-close>
    <el-form :model="data.form" :rules="rules" ref="formRef" label-width="100px" label-position="right" style="padding-right: 30px">
      <el-form-item label="学生账号" prop="username">
        <el-input v-model="data.form.username" autocomplete="off" />
      </el-form-item>
      <el-form-item label="学生密码" prop="password">
        <el-input show-password v-model="data.form.password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="学生名称">
        <el-input v-model="data.form.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="data.form.phone" autocomplete="off" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="data.form.email" autocomplete="off" />
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="data.form.sex">
          <el-radio label="男"></el-radio>
          <el-radio label="女"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="生日">
        <el-date-picker style="width: 100%" format="YYYY-MM-DD" value-format="YYYY-MM-DD" v-model="data.form.birth"></el-date-picker>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="data.form.address" autocomplete="off" />
      </el-form-item>
      <el-form-item label="学生头像">
        <el-upload action="http://localhost:9090/files/upload" list-type="picture" :on-success="handleImgUploadSuccess">
          <el-button>上传头像</el-button>
        </el-upload>
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

const baseUrl = '/student'


const data = reactive({
  username: '',
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 10,
  name: '',
  formVisible: false,
  form: {},
})

// 数据加载函数
const load = () => {
  request.get(baseUrl + '/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      username: data.username,
      name: data.name,
    }
  }).then(res => {
    data.tableData = res.data?.list || []   // 没有data时不报错，加个问号
    data.total = res.data?.total || 0
  })
}

// 页码样式
const background = ref(false)
const disabled = ref(false)


// 调用方法获取后台数据
load();
// 页码和Size变化时的响应方法
const handleCurrentChange = () => {
  // 重新加载数据即可
  load()
}

const handleSizeChange = () => {
  load()
}
// 重置查询操作
const reset = () => {
  data.username = ''
  data.name = ''
  load()
}

// 登录校验
const rules = reactive({
  username: [
    {required: true, message: '请输入账号', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
  ]
})

const formRef = ref();

// 弹窗
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

// 响应新增或者更新操作，通过是否有id字段进行分辨
const save = () => {
  formRef.value.validate((valid) => {
    if(valid){
      request.request({
        url: data.form.id ? baseUrl + '/update' : baseUrl + '/add',
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
  })
}

// 编辑修改操作
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 删除操作
const handleDelete = (id) => {
  ElMessageBox.confirm("删除数据后无法恢复，您确认删除吗？", "删除确认", { type: 'warning'}).then(res => {
    request.delete(baseUrl + '/delete/' + id).then(res => {
      if(res.code === '200'){
        // 重新获取数据
        load();
        data.formVisible = false
        ElMessage.success("操作成功")
      } else{
        ElMessage.error(res.msg);
      }
    })
  }).catch(res => {
    /*ElMessage({
      type: 'info',
      message: 'Delete canceled',
    })*/
  })
}

const handleImgUploadSuccess = (res) => {
  data.form.avatar = res.data
}
</script>