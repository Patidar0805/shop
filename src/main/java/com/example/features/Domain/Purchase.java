package com.example.features.Domain;


    public class Purchase {
        private int productId;
        private int quantity;
        private String customerName;



        public Purchase(int productId, int quantity, String customerName) {
            this.productId = productId;
            this.quantity = quantity;
            this.customerName = customerName;
        }

        // Getters & Setters

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }
    }
