<template>
  <div class="login_container">
      <div class="login_box">
          <!-- 头像区域 -->
          <div class="avatar_box">
            <img src="../assets/1.png" alt />
          </div>
          <!-- 登录表单区域 -->
          <el-form ref="loginFormRef" :model="loginForm" :rules="loginFormRules" label-width="0px" class="login_form">
            <!-- 用户名 -->
            <el-form-item prop="username">
                <el-input v-model="loginForm.username" prefix-icon="iconfont icon-user"></el-input>
            </el-form-item>
            <!-- 密码 -->
            <el-form-item prop="password">
                <el-input v-model="loginForm.password" prefix-icon="iconfont icon-3702mima" type="password"></el-input>
            </el-form-item>
            <!-- 按钮 -->
            <el-form-item class="btn">
                <el-button type="primary" @click="login">登录</el-button>
                <el-button type="info" @click="dialogTableVisible = true">注册</el-button>
            </el-form-item>
          </el-form>
      </div>
    <el-dialog
      title="注册"
      :visible.sync="dialogTableVisible"
      :close-on-click-modal="false"
      style="text-align: center"
      @close="addDialogClose"
    >
      <!-- 添加用户的表单 -->
      <el-form ref="addForm" :model="addUser" :rules="addUserRules">
        <el-form-item prop="username" label="昵称">
          <el-input v-model="addUser.userName" placeholder="请输入位用户名" />
        </el-form-item>
        <el-form-item prop="password" label="密码">
          <el-input v-model="addUser.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item prop="username" label="姓名">
          <el-input v-model="addUser.studentName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item prop="username" label="学号">
          <el-input v-model="addUser.studentId" placeholder="请输入真实学号" />
        </el-form-item>
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
          <div class="el-upload__tip" slot="tip">请上传一卡通或学生证照片，只能上传jpg文件，且不超过2MB</div>
        </el-upload>
        <el-form-item style="margin-top: 20px">
          <el-button @click="dialogTableVisible = false">取消</el-button>
          <el-button type="primary" @click="onAddUser" :disabled=flag>确定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import _ from "lodash";

export default {
  data () {
    return {
    // 登录表单的数据绑定对象
      loginForm: {
        username: '',
        password: ''
      },
      // 表单的验证规则对象
      loginFormRules: {
        // 验证用户名是否合法
        username: [
          { required: true, message: '请输入学号', trigger: 'blur' },
          { min: 1, max: 11, message: '长度为 11 个数字', trigger: 'blur' }
        ],
        // 验证密码是否合法
        password: [
          { required: true, message: '请输入登录密码', trigger: 'blur' },
          { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
        ]
      },
      addUserRules: {
        // 验证用户名是否合法
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 1, max: 6, message: '长度为 1 到 6 个字符', trigger: 'blur' }
        ],
        // 验证密码是否合法
        password: [
          { required: true, message: '请输入登录密码', trigger: 'blur' },
          { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
        ],
        studentName: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' },
          { min: 2, max: 6, message: '长度在 2 到 6 个字符', trigger: 'blur' }
        ],
        studentId: [
          { required: true, message: '请输入学号', trigger: 'blur' },
          { min: 11, max: 11, message: '长度为 11 个数字', trigger: 'blur' }
        ]
      },
      dialogTableVisible: false, // 添加用户弹框
      addUser: {
        userName: '',
        password: '',
        studentId: '',
        studentName: '',
        userPhotoUrl: '',
      },
      // 上传图片的url
      uploadURL: 'api/order/uploadPosterPhoto',
      // 图片上传组件的headers请求头对象
      headersObj: {
        Authorization: window.sessionStorage.getItem('token')
      },
      flag: true
    }
  },
  methods: {
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
      this.addUser.userPhotoUrl = response.data
      this.flag = false
    },
    login () {
      this.$refs.loginFormRef.validate(async (valid) => {
        // console.log(valid)
        if (!valid) return 0
        const { data: res } = await this.$http.get('api/login', { params: { studentId: this.loginForm.username, password: this.loginForm.password } })
        const { code } = res
        const { data } = res
        if (code !== '200') return this.$message.error('登录失败！')
        this.$message.success('登录成功！')
        // console.log(res)
        window.sessionStorage.setItem('token', data.token)
        window.sessionStorage.setItem('userId', data.userId)
        window.sessionStorage.setItem('userName', data.userName)
        await this.$router.push('/home')
        // console.log(res)
        // 1.将登录成功之后的 token，保存到客户端的 sessionStorage 中
        //   1.1项目中除了登录之外的其它api接口，必须在登录之后才能访问
        //   1.2token只应在当前网站打开期间生效，所以将 token 保存在sessionStorage 中
        // 2.通过编程式导航跳转到后台主页，路由地址是 /home
      })
    },
    // 关闭弹框的回调
    addDialogClose() {
      Object.assign(this.$data, this.$options.data()) // 清空表单
    },
    // 点击添加用户
    async onAddUser() {
      const form = _.cloneDeep(this.addUser)
      form.studentId = Number(form.studentId)
      const { data: res } = await this.$http.post('api/register', form )
      const { code } = res
      const { data } = res
      if (code !== '200') return this.$message.error('注册失败！')
      this.$message.success('注册成功！')
      // window.sessionStorage.setItem('token', data)
      this.addDialogClose()
      await this.$router.push('/login')
    },
  }
}
</script>

<style lang="less" scoped>
.login_container{
    background-color: #2b4b6b;
    height: 100%;
}
.login_box{
    width: 450px;
    height: 300px;
    background-color: #fff;
    border-radius: 3px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
    .avatar_box{
        height: 127px;
        width: 212px;
        border: 1px solid #eee;
        border-radius: 10%;
        padding: 10px;
        box-shadow: 0 0 10px #ddd;
        position: absolute;
        left: 50%;
        transform: translate(-50%,-50%);
        background-color: #fff;
        img{
            width: 100%;
            height: 100%;
            border-radius: 10%;
            background-color: #eee;
        }
    }
}
.btn{
    display: flex;
    justify-content: flex-end;
}
.login_form{
    position: absolute;
    bottom: 0;
    width: 100%;
    padding: 0 20px;
    box-sizing: border-box;
}
</style>
