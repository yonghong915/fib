<template>
	<div>
		<ul>
			<li v-for="item in dataList" :key="item.id">{{ item.name }}</li>
		</ul>
	</div>
</template>

<script>
import axios from 'axios';

export default {
	name: 'MyComponent',
	data() {
		return {
			dataList: [] // 存储返回的数据
		}
	},
	mounted() {
		axios.get('http://localhost:8080/cmms/chineseMedicine/list'
			.then(response => {
				// 处理响应结果
				this.dataList = response.data;
			}).catch(error => {
				// 处理错误
				console.error(error);
			})
		);
	}
}
</script>

<style>
/* 这里是组件的样式 */
</style>