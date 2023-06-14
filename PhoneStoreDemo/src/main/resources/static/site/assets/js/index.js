var vue = new Vue({
    el: '#myheader',
    data: {
        listBrand: []
    },
    methods: {
        getAllBrands() {

            var self = this

            axios.get('/dtdd/getAllBrand')
                .then(res => {
                    self.listBrand = res.data
                })
                .catch(error => {

                })

        },
    },
    created() {
        this.getAllBrands()
    }

})