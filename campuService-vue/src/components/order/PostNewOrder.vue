<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>发布订单</el-breadcrumb-item>
      <el-breadcrumb-item>我要发单</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图 -->
    <el-card>
      <!-- 提示区域 -->
      <el-alert title="添加订单信息" type="info" center show-icon :closable="false"></el-alert>
      <!-- 步骤条区域 -->
      <el-steps :active="activeIndex - 0" finish-status="success" align-center>
        <el-step title="基本信息"></el-step>
        <el-step title="上传图片"></el-step>
        <el-step title="完成"></el-step>
      </el-steps>
      <!-- form表单 -->
      <el-form
        ref="addFormRef"
        :model="addForm"
        :rules="addFormRules"
        label-width="70px"
        label-position="top"
        style="margin: 0 auto; width: 900px"
      >
        <!-- tabs栏区域 -->
        <el-tabs
          v-model="activeIndex"
          :tab-position="tabPosition"
          :before-leave="beforeTabLeave"
        >
          <el-tab-pane label="基本信息" name="0">
            <el-form-item prop="start" label="起点">
              <el-select v-model="addForm.start" placeholder="请选择起点">
                <el-option
                  v-for="item in map"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="destination" label="终点">
              <el-select v-model="addForm.destination" placeholder="请选择终点">
                <el-option
                  v-for="item in map"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="price" label="价格">
              <el-input v-model="addForm.price" type="number"></el-input>
            </el-form-item>
            <el-form-item prop="description" label="描述">
              <el-input v-model="addForm.description"></el-input>
            </el-form-item>
            <el-form-item prop="goods_cat" label="大小">
              <el-radio-group v-model="addForm.size">
                <el-radio :label="0" border>大（大于2kg）</el-radio>
                <el-radio :label="1" border>中（1到2kg）</el-radio>
                <el-radio :label="2" border>小（小于1kg）</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="上传图片" name="1">
            <div style="margin: 0 auto; width: 500px">
            <!-- action表示图片上传后台api地址 -->
<!--            <el-upload-->
<!--              :action="uploadURL"-->
<!--              :on-preview="handlePreview"-->
<!--              :on-remove="handleRemove"-->
<!--              list-type="picture"-->
<!--              :headers="headersObj"-->
<!--              :on-success="handlerSuccess"-->
<!--            >-->
<!--              <el-button size="small" type="primary">点击上传</el-button>-->
<!--              <div slot="tip" class="el-upload__tip">只能上传一个jpg/png文件，且不超过500kb</div>-->
<!--            </el-upload>-->
              <el-upload
                class="upload-demo"
                drag
                :action="uploadURL"
                :headers="headersObj"
                :on-success="handlerSuccess"
                :before-upload="beforeUpload"
                :limit=1  >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip" slot="tip">只能上传jpg文件，且不超过2MB</div>
              </el-upload>
            </div>
          </el-tab-pane>
          <el-tab-pane label="新建订单" name="2">
            <div style="margin: 0 auto; width: 100px">
              <el-button type="primary" class="btnAdd" @click="add">发布</el-button>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </el-card>
    <!-- 图片预览 -->
    <el-dialog title="图片预览" :visible.sync="previewVisible" width="50%">
      <img :src="previewPath" class="previewImg" />
    </el-dialog>
  </div>
</template>

<script>
  import _ from 'lodash'

  export default {
    data() {
      return {
        map: [{
          value: 0,
          label: '邯郸'
        }, {
          value: 1,
          label: '江湾'
        }, {
          value: 2,
          label: '枫林'
        }, {
          value: 3,
          label: '张江'
        }],
        // 步骤条下标
        activeIndex: '0',
        // tabs标签栏居左显示
        tabPosition: 'left',
        addForm: {
          start: null,
          destination: null,
          price: null,
          description: '',
          size: 0,
          // 图片的数组
          posterPhotoUrl: 'url',
          posterId: Number(window.sessionStorage.getItem('userId')),
        },
        addFormRules: {
          price: [
            { required: true, message: '请输入价格', trigger: 'blur' }
          ],
          description: [
            { required: true, message: '请输入描述', trigger: 'blur' }
          ]
        },
        // 上传图片的url
        uploadURL: 'api/order/uploadPosterPhoto',
        // 图片上传组件的headers请求头对象
        headersObj: {
          Authorization: window.sessionStorage.getItem('token')
        },
        // 预览路径
        previewPath: '',
        // 是否预览
        previewVisible: false
      }
    },
    created() {
    },
    methods: {
      beforeTabLeave(activeName, oldActiveName) {
        if (oldActiveName === '0' && (this.addForm.description === '' || this.addForm.price === null || this.addForm.start === null || this.addForm.destination === null || this.addForm.start === this.addForm.destination)) {
          this.$message.error('请正确填写表单！')
          return false
        }
      },
      beforeUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      },
      // 监听图片上传成功的事件
      handlerSuccess(response) {
        // this.addForm.pics = response.data.url
        this.addForm.posterPhotoUrl = response.data
      },
      // 监听添加商品
      add() {
        this.$refs.addFormRef.validate(async valid => {
          const form = _.cloneDeep(this.addForm)
          form.price = Number(form.price)
          const { data: res } = await this.$http.post('api/order/postOrder', form)
          const { code } = res
          if (code !== '200') {
            return this.$message.error('发布订单失败！')
          }
          this.$message.success('发布订单成功！')
          await this.$router.push('/wait')
        })
      }
    }
  }
</script>

<style lang="less" scoped>
  .el-checkbox {
    margin: 0 10px 0 0 !important;
  }
  .previewImg {
    width: 100%;
  }
  .btnAdd {
    margin-top: 15px;
  }
</style>
