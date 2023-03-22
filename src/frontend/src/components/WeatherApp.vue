<template>
  <main>

    <form @submit.prevent="getWeather">
      <input type="text" v-model="cityInput" placeholder="Enter city name">
      <button type="submit">Get weather</button>
    </form>

    <section v-if="weatherData">
      <div class="cloudiness">
        <img src="../assets/icons/cloud.svg" alt="cloudiness"><span class="cloudiness__value">{{ 3131 }}</span>&percnt;
      </div>
      <div class="wind-speed">
        <img src="../assets/icons/wind.svg" alt="wind speed"><span class="wind__value">{{ weatherData.windSpeed }}</span>m/s
      </div>
      <div class="humidity">
        <img src="../assets/icons/humidity.svg" alt="humidity"><span class="humidity__value">{{ weatherData.mainHum }}</span>&percnt;
      </div>
    </section>

    <section v-if="weatherData">
      <div class="temperature__value">{{ weatherData.mainTemp }}</div>
      <div class="temperature__right">
        <div class="temperature__scale">
          <p>&deg;C</p>
        </div>
        <div class="temperature__high">
          <img src="../assets/icons/high.svg" alt="high temperature"><span>{{ 3131 }}</span>&deg;
        </div>
        <div class="temperature__low">
          <img src="../assets/icons/low.svg" alt="low temperature"><span>{{3131}}</span>&deg;
        </div>
      </div>
    </section>

    <section v-if="weatherData">
      <div class="location">{{ weatherData.name }}, {{weatherData.sysCountry}}</div>
      <div class="weather__description">{{ weatherData.weatherDesc }}</div>
    </section>

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


<style scoped>
section {
  width: 100%;
  padding-top: 25px;
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
}
.cloudiness img {
  width: 48px;
  height: 48px;
  vertical-align: middle;
}
.wind-speed img {
  width: 48px;
  height: 48px;
  vertical-align: middle;
}
.humidity img {
  width: 48px;
  height: 48px;
  vertical-align: middle;
}

.temperature__value {
  font-size: 7em;
  color: rgba(255, 255, 255, 0.75);
}
.temperature__right {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.temperature__scale {
  padding-top: 5px;
  font-size: 2em;
  font-weight: bold;
  color: rgba(255, 255, 255, 0.75);
}
.temperature__high {
  padding-top: 5px;
}
.temperature__high img {
  vertical-align: middle;
}
.temperature__low img {
  vertical-align: middle;
}

.location {
  text-transform: uppercase;
  font-weight: bold;
}
.weather__description {

  text-transform: lowercase;
  margin-left: 10%;
  margin-right: 10%;
  text-align: center;
}
.weather__description:first-letter {
  text-transform: uppercase;
}
.weather__icon {
  width: 12em;
  padding-bottom: 9em;
}
</style>