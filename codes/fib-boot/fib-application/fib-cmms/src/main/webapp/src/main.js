import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 新增代码：引入全部组件及样式
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'

// 新增代码：注册全部组件
Vue.use(Antd)

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#app')