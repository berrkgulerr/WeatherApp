<template>
  <main>
    <form @submit.prevent="getWeather">
      <input type="text" v-model="cityInput" placeholder="Enter city name">
      <button type="submit">Get weather</button>
    </form>

    <Measurements v-if="weatherData"
        :cloudiness="3131"
        :windSpeed= "this.weatherData.windSpeed"
        :humidity= "this.weatherData.mainHum"
    ></Measurements>

    <Temperature v-if="weatherData"
        :value="this.weatherData.mainTemp"
        :high="3131"
        :low="3131"
    ></Temperature>

    <Weather v-if="weatherData"
        :location="this.weatherData.name +  ', ' + this.weatherData.sysCountry"
        :description="this.weatherData.weatherDesc"
        :icon="require('../assets/icons/weather/cloud.svg')"
    ></Weather>

    <form @submit.prevent="setFrequency">
      <label for="frequency-select">Frequency:</label>
      <select id="frequency-select" v-model="frequency">
        <option value="1000">1 second</option>
        <option value="60000">1 minute</option>
        <option value="3600000">1 hour</option>
      </select>
      <button type="submit">Update frequency</button>
    </form>

  </main>
</template>

<script>
import axios from 'axios';
import Weather from './Weather'
import Temperature from './Temperature'
import Measurements from './Measurements'

export default {
  name: 'WeatherApp',
  components: {
    Weather,
    Temperature,
    Measurements
  },

  data() {
    return {
      city: '',
      weatherData: null,
      intervalId: null,
      frequency: '1000', // default frequency is 1 second
      cityInput: ''
    };
  },

  methods: {
    getWeather(event) {
      event.preventDefault(); // prevent default form submission behavior
      this.city = this.cityInput;
      axios.get('/weather?city=' + this.city)
          .then(response => {
            console.log(response.data)
            this.weatherData = response.data;
          })
          .catch(error => {
            console.error(error);
          });
      this.setFrequency();
    },

    startPeriodicUpdate() {
      this.intervalId = setInterval(() => {
        axios.get('/weather?city=' + this.city)
            .then(response => {
              console.log(response.data)
              this.weatherData = response.data;
            })
            .catch(error => {
              console.error(error);
            });
      }, this.frequency);
    },

    setFrequency() {
      clearInterval(this.intervalId);
      this.startPeriodicUpdate();
    }
  },
  mounted() {
    axios.get('/weather')
        .then(response => {
          console.log(response.data);
          this.weatherData = response.data;
          this.city = this.weatherData.name;
          this.setFrequency();
        })
        .catch(error => {
          console.error(error);
        });
  },
  beforeUnmount() {
    clearInterval(this.intervalId);
  }
};
</script>

<style scoped>
main {
  width: 100%;
  height: 100%;
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  box-shadow: 0 19px 38px rgba(0,0,0,0.30), 0 15px 12px rgba(0,0,0,0.22);
}


.app--day {
  background-image: linear-gradient(rgba(0, 0, 0, 0.1), rgba(0, 0, 0, 0.1)), url("../assets/images/morning.jpg");
}
.app--night {
  background-image: linear-gradient(rgba(0, 0, 0, 0.1), rgba(0, 0, 0, 0.1)), url("../assets/images/night.jpg");
}

@media screen and (min-width: 450px) {
  main {
    width: 330px;
    height: 600px;
    border-radius: 5px
  }
}
</style>
