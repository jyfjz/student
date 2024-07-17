import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'  // 引入图标控件
// 引入echarts
import echarts from "./utils/echarts";

import '@/assets/css/global.css'

const app = createApp(App)

app.config.globalProperties.$echarts = echarts; // vue3的挂载方式（一个用于注册能够被应用内所有组件实例访问到的全局属性的对象。）

app.provide('$echarts', echarts); // vue3采用provide, inject方式使用


app.use(router)
app.use(ElementPlus, {
    locale: zhCn,
})
app.mount('#app')

// 循环访问显示控件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}