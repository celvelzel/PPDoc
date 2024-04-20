<script>
import axios from 'axios'

export default {
  data() {
    return {
      dialogFormVisible: false,
      docInfoForm: {
        document_name:'',
        document_type:'',
        all_info:'',
      },
      options:[],
      formLabelWidth: '120px'
    };
  },
  props:{

  },

  methods: {
    onSubmit() {
      this.dialogFormVisible = false
    },
  },
  mounted() {
    axios.get('http://localhost:8080/docs/types'
    ).then(res => {
      this.options = res.data.data;
    });
  },
};
</script>

<template>
  <div>
    <!-- Form -->
    <el-button type="text" @click="dialogFormVisible = true">编辑</el-button>

    <el-dialog title="编辑文档" :visible.sync="dialogFormVisible">
      <el-form :model="docInfoForm">
        <el-form-item label="所有信息" :label-width="formLabelWidth">
          <el-input v-model="docInfoForm.document_name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="所有信息" :label-width="formLabelWidth">
          <el-input v-model="docInfoForm.all_info" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="文档类型" :label-width="formLabelWidth" style="text-align: left">
          <el-select v-model="docInfoForm.document_type" placeholder="请选择" style="text-align: left">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click=onSubmit>确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>

</style>
