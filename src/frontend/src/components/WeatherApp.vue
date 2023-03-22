<template>
  <form @submit.prevent="getWeather">
    <input type="text" v-model="cityInput" placeholder="Enter city name">
    <button type="submit">Get weather</button>
  </form>

  <div v-if="weatherData">
    <h1>{{ weatherData.name }}</h1>
    <p>{{ weatherData.mainTemp }}</p>
    <p>{{ weatherData.weatherDesc }} &deg;C</p>
  </div>

  <form @submit.prevent="setFrequency">
    <label for="frequency-select">Frequency:</label>
    <select id="frequency-select" v-model="frequency">
      <option value="1000">1 second</option>
      <option value="60000">1 minute</option>
      <option value="3600000">1 hour</option>
    </select>
    <button type="submit">Update frequency</button>
  </form>
</template>

<script>
import axios from 'axios';

export default {
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
