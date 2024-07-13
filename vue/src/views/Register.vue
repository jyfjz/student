<template>
  <div>
    <div class="login-container">
      <div style="width: 350px" class="login-box">
        <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 25px;
              color: #fff">注 册</div>
        <el-form :model="data.form" ref="FormRef" :rules="rules">
          <el-form-item prop="username">
            <el-input prefix-icon="User" v-model="data.form.username" placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password prefix-icon="Lock" v-model="data.form.password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item >
            <el-button type="primary" style="width: 100%" @click="register">注 册</el-button>
          </el-form-item>
          </el-form>
        <div style="margin-top: 25px; text-align: right; color: #fff">
          已经有账号?请<a href="/login" >登录</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 绑定
import {reactive, ref} from "vue"
import request from "../utils/request";
import {ElMessage} from "element-plus";
import router from "../router";

const data = reactive({
    form: {},
})
// 登录校验
const rules = reactive({
  username: [
    {required: true, message: '请输入账号', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
  ]
})

const FormRef = ref()

const register = () =>{
  FormRef.value.validate((valid) => {
    if(valid){
      request.post('/register', data.form).then(res => {
        if(res.code === '200'){
          ElMessage.success('注册成功')
          console.log(res.data)
          router.push('/login')   // 跳转到登录界面
        }
        else{
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

</script>

<style scoped>
.login-container {
  min-height: 100vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url("../assets/imgs/register.jpg");
  background-size: cover;
}
.login-box{
  background-color: rgba(0, 0, 0, .6);
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 30px;
}
a {
   text-decoration: none;
   color: #000;
}

/* 状态一: 未被访问过的链接 */
a:link {
  text-decoration: none;
  color: #f5f7fa;
}

/* 状态二: 已经访问过的链接 */
a:visited {
  text-decoration: none;
  color: #d51414;
}

/* 状态三: 鼠标划过(停留)的链接(默认红色) */
a:hover {
  text-decoration: none;
  color: #b40d0d;
}
/* 状态四: 被点击的链接 */
a:active {
  text-decoration: none;
  color: #5b1111;
}
</style>