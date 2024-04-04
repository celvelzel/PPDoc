<template>
  <div id="app">
    <invoiceView></invoiceView>
  </div>
</template>

<script>
import invoiceView from "@/views/InvoiceView.vue";
export default {
  name: 'App',
  components: {
    invoiceView
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
