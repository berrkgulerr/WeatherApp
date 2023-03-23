<template>
  <main>
    <div v-if="errorMessage" class="error-message">
    {{ errorMessage }}
    </div>

    <div class="search-box" @keydown.enter.prevent="getWeather">
      <input class="search-bar" type="text" v-model="cityInput" placeholder="Search city...">
    </div>

    <Measurements v-if="weatherData"
        :cloudiness="this.weatherData.cloudsAll"
        :windSpeed= "this.weatherData.windSpeed"
        :humidity= "this.weatherData.mainHum"
        :feelsLike= "this.weatherData.mainFeels"
    ></Measurements>

    <Temperature v-if="weatherData"
        :value="this.weatherData.mainTemp"
        :high="this.weatherData.mainTempMax"
        :low="this.weatherData.mainTempMin"
    ></Temperature>

    <Weather v-if="weatherData"
        :location="this.weatherData.name +  ', ' + this.weatherData.sysCountry"
        :description="this.weatherData.weatherDesc"
        :icon="getIcon()"
    ></Weather>

    <div v-if="weatherData">
      <p>Timestamp: {{this.weatherData.timeStamp}}</p>
    </div>
    
    <div class="container d-flex justify-content-center"  >
      <form class="row g-2 align-items-center">
        <div class="col-auto">
          <label for="frequency-select" class="form-label">Refresh Rate:</label>
        </div>
        <div class="col-auto">
          <select id="frequency-select" v-model="frequency" class="form-select" @change="setFrequency">
            <option value="1000">1 second</option>
            <option value="60000">1 minute</option>
            <option value="3600000">1 hour</option>
          </select>
        </div>
      </form>
    </div>

  </main>
</template>

<script>
import axios from 'axios';
import Weather from './Weather'
import Temperature from './Temperature'
import Measurements from './Measurements'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
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
      frequency: '60000', // default frequency is 1 minute
      cityInput: '',
      errorMessage: null
    };
  },

  methods: {
    getWeather(event) {
      event.preventDefault(); // prevent default form submission behavior
      axios.get('/weather?city=' + this.cityInput)
          .then(response => {
            console.log(response.data)
            if(response.data){
              this.city = this.cityInput;
              this.weatherData = response.data;
            } 
            else {
              this.errorMessage = 'Please Give Valid City Name.'
              setTimeout(() => {
                this.errorMessage = null
              }, 5000)
            }
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
              if(response.data) this.weatherData = response.data;
              else {
                this.errorMessage = 'An error occurred. Please try again later.'
                setTimeout(() => {
                  this.errorMessage = null
                }, 5000)
              }
            })
            .catch(error => {
              console.error(error);
            });
      }, this.frequency);
    },

    setFrequency() {
      clearInterval(this.intervalId);
      this.startPeriodicUpdate();
    },

    getIcon(){
      switch(this.weatherData.weatherIcon){
        case '01d': return require('../assets/icons/weather/animated/day.svg')
        case '01n': return require('../assets/icons/weather/animated/night.svg')

        case '02d': return require('../assets/icons/weather/animated/cloudy-day-3.svg')
        case '02n': return require('../assets/icons/weather/animated/cloudy-night-3.svg')

        case '03d': return require('../assets/icons/weather/animated/cloudy.svg')
        case '03n': return require('../assets/icons/weather/animated/cloudy.svg')

        case '04d': return require('../assets/icons/weather/animated/cloudy.svg')
        case '04n': return require('../assets/icons/weather/animated/cloudy.svg')

        case '09d': return require('../assets/icons/weather/animated/rainy-7.svg')
        case '09n': return require('../assets/icons/weather/animated/rainy-7.svg')

        case '10d': return require('../assets/icons/weather/animated/rainy-1.svg')
        case '10n': return require('../assets/icons/weather/animated/rainy-4.svg')

        case '11d': return require('../assets/icons/weather/animated/thunder.svg')
        case '11n': return require('../assets/icons/weather/animated/thunder.svg')

        case '13d': return require('../assets/icons/weather/animated/snowy-5.svg')
        case '13n': return require('../assets/icons/weather/animated/snowy-5.svg')

        case '50d': return require('../assets/icons/weather/animated/mist.svg')
        case '50n': return require('../assets/icons/weather/animated/mist.svg')

        default: return require('../assets/icons/weather/animated/weather.svg')
      }
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
.error-message {
  position: fixed;
  top: 1em;
  right: 1em;
  padding: 1em;
  background-color: red;
  border-radius: 16px 16px 16px 16px;
  color: white;
  z-index: 999;
}
.search-box {
  width: 100%;
  margin-bottom: 30px;
}
.search-box .search-bar {
  display: block;
  width: 100%;
  padding: 15px;
  
  color: #313131;
  font-size: 20px;
  appearance: none;
  border:none;
  outline: none;
  background: none;
  box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.25);
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 0px 16px 0px 16px;
  transition: 0.4s;
}
.search-box .search-bar:focus {
  box-shadow: 0px 0px 16px rgba(0, 0, 0, 0.25);
  background-color: rgba(255, 255, 255, 0.75);
  border-radius: 16px 0px 16px 0px;
}

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
