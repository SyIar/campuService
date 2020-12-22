<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>接手订单</el-breadcrumb-item>
      <el-breadcrumb-item>进行中订单</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图 -->
    <el-card>
      <el-table :data="ordersList" border stripe>
        <template slot="empty">
          <span>暂无数据</span>
        </template>
        <el-table-column type="index" width="30px"></el-table-column>
        <el-table-column label="订单ID" prop="orderId" width="60px"></el-table-column>
        <el-table-column label="起点">
          <template slot-scope="scope">
            <el-tag>{{ scope.row.start | mapFilter }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="终点">
          <template slot-scope="scope">
            <el-tag>{{ scope.row.destination | mapFilter }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="大小">
          <template slot-scope="scope">
            <span>{{ scope.row.size | sizeFilter }}</span>
          </template>
        </el-table-column>
        <el-table-column label="发单方ID" prop="posterId"></el-table-column>
        <el-table-column label="价格" prop="price" width="60px"></el-table-column>
        <el-table-column label="发单时间" prop="postTime"></el-table-column>
        <el-table-column label="接单时间" prop="acceptTime"></el-table-column>
        <el-table-column label="完成时间" prop="finishTime"></el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            <span>{{ scope.row.status | statusFilter }}</span>
          </template>
        </el-table-column>
        <el-table-column label="接单方文件">
          <template slot-scope="scope">
            <a :href="scope.row.accepterPhotoUrl" target="_blank"><el-button size="mini">点击查看</el-button></a>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleConfirm(scope.row.orderId)">确认</el-button>
            <el-button size="mini" @click="handleRefuse(scope.row.orderId)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
  export default {
    filters: {
      mapFilter(index) {
        const map = {
          0: '邯郸',
          1: '江湾',
          2: '枫林',
          3: '张江'
        }
        return map[index]
      },
      sizeFilter(index) {
        const map = {
          0: '大型',
          1: '中型',
          2: '小型'
        }
        return map[index]
      },
      statusFilter(index) {
        const map = {
          0: '等待接单',
          1: '等待运送',
          2: '等待确认',
          3: '完成',
          4: '拒绝'
        }
        return map[index]
      }
    },
    data() {
      return {
        ordersList: []
      }
    },
    created() {
      this.getWaitingOrdersList()
    },
    methods: {
      async getWaitingOrdersList() {
        const { data: res } = await this.$http.get('api/order/getWaitingOrder')
        const { code } = res
        const { data } = res
        if (code !== '200') {
          return this.$message.error('获取订单列表失败！')
        }
        this.ordersList = data
        this.ordersList.forEach(function(elem) {
          elem['postTime'] = elem['postTime'].replace('T', ' ')
          elem['postTime'] = elem['postTime'].substr(0, elem['postTime'].indexOf('.'))
          if (elem['acceptTime'] !== null) {
            elem['acceptTime'] = elem['acceptTime'].replace('T', ' ')
            elem['acceptTime'] = elem['acceptTime'].substr(0, elem['acceptTime'].indexOf('.'))
          }
          if (elem['finishTime'] !== null) {
            elem['finishTime'] = elem['finishTime'].replace('T', ' ')
            elem['finishTime'] = elem['finishTime'].substr(0, elem['finishTime'].indexOf('.'))
          }
        })
      },
      async handleConfirm(orderId) {
        const {data: res} = await this.$http.get('api/order/confirmOrder', { params: { orderId: orderId }})
        const {code} = res
        if (code !== '200') {
          return this.$message.error('操作失败！')
        }
        this.$message.success('操作成功！')
        await location.reload()
      },
      async handleRefuse(orderId) {
        const {data: res} = await this.$http.get('api/order/refuseOrder', { params: { orderId: orderId }})
        const {code} = res
        if (code !== '200') {
          return this.$message.error('操作失败！')
        }
        this.$message.success('操作成功！')
        await location.reload()
      }
    }
  }
</script>

<style>

</style>
