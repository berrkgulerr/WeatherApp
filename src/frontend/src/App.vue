<template>
  <div id="app">
    <div class="app">
      <WeatherApp></WeatherApp>
    </div>
  </div>
</template>

<script>
import WeatherApp from './components/WeatherApp'
export default {
  name: 'App',
  components: {
    WeatherApp
  }
}
</script>

<style>
/** Global **/
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}
body {
  font-family: 'Open Sans', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: rgba(255, 255, 255, 0.9);
}
a {
  text-decoration: none;
  color: inherit;
  transition: color .2s ease-in;
}

.app {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #6580be;
}
</style>
