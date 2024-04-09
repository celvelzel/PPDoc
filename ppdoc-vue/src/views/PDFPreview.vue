<template>
  <PDFViewer :pdfUrl="url"></PDFViewer>
</template>

<script>
import PDFViewer from "@/components/PDFViewer.vue";

export default {
  data() {
    return {
      // url: 'http://localhost:8080/pdf/test.pdf',
      url: 'https://raw.githubusercontent.com/mozilla/pdf.js/ba2edeae/web/compressed.tracemonkey-pldi-09.pdf',
    };
  },
  components: {
    PDFViewer
  }
};
</script>
<style>

</style>
