<template>
  <div>
<!--  <PDFViewer :PdfUrl="this.url"></PDFViewer>-->
<!--  file后携带需要展示的pdf链接-->
  <iframe :src="`static/pdf/web/viewer.html?file=`+url" width="800" height="1000"></iframe>

  <!--  http://localhost:8080/pdf/example.pdf
<!--  <iframe id="pdfContainer" :src="`/static/pdf/web/viewer.html?file=https://raw.githubusercontent.com/mozilla/pdf.js/ba2edeae/web/compressed.tracemonkey-pldi-09.pdf`" width="100%" height="100%" frameborder="0" />-->
  </div>
</template>

<script>
// import PDFViewer from "@/components/PDFViewer.vue";

export default {
  data() {
    return {
      // url: 'http://localhost:8080/pdf/test.pdf',
      url: "http://localhost:8080/pdf/example.pdf",
    };
  },
  components: {
    // PDFViewer
  }
};
</script>
<style>

</style>
