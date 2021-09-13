module.exports = {
    // options...
    devServer: {
          proxy: 'http://localhost:8080/',
          port: '5050'
      },

    transpileDependencies: [
      'vuetify'
    ]
}
