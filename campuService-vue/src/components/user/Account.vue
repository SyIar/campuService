<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>个人中心</el-breadcrumb-item>
      <el-breadcrumb-item>个人信息</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图 -->
    <el-card>
      <div class="account-text">
        昵称：{{ userName }}
        <br>账号：{{ userId }}
        <br>余额：{{ balance }}
        <br>姓名：{{ studentName }}
        <br>学号：{{ studentId }}
      </div>
    </el-card>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        userName: window.sessionStorage.getItem('userName'),
        userId: window.sessionStorage.getItem('userId'),
        balance: window.sessionStorage.getItem('balance'),
        studentName: window.sessionStorage.getItem('studentName'),
        studentId: window.sessionStorage.getItem('studentId'),
      }
    },
    created() {
    },
    methods: {
    }
  }
</script>

<style>
  .account-text {
      margin: 30px;
      font-size: 20px;
      line-height: 30px;
  }
</style>
