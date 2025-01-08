-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 08, 2025 at 07:07 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `supermarket_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `bill_report`
--

CREATE TABLE `bill_report` (
  `bill_id` int(11) NOT NULL,
  `item_name` varchar(100) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price_per_item` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `bill_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bill_report`
--

INSERT INTO `bill_report` (`bill_id`, `item_name`, `quantity`, `price_per_item`, `subtotal`, `bill_date`) VALUES
(1, 'coca', 5, 25.00, 125.00, '2024-12-22 11:58:07'),
(2, 'coca', 2, 25.00, 50.00, '2024-12-26 11:25:03'),
(3, 'sprite', 2, 25.00, 50.00, '2024-12-26 12:32:14'),
(4, 'cookies', 2, 2.50, 5.00, '2024-12-26 12:36:37');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `quantity` int(11) NOT NULL,
  `category` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `price`, `quantity`, `category`) VALUES
(1, 'coca', 25.00, 4, 'soda'),
(2, 'bread', 10.00, 12, 'bread'),
(3, 'pork', 150.00, 1, 'meat'),
(4, 'apple', 1.00, 50, 'fruit'),
(5, 'banana', 0.50, 100, 'fruit'),
(6, 'orange', 0.75, 80, 'fruit'),
(7, 'chicken', 120.00, 5, 'meat'),
(8, 'beef', 200.00, 2, 'meat'),
(9, 'milk', 1.50, 20, 'dairy'),
(10, 'cheese', 5.00, 15, 'dairy'),
(11, 'yogurt', 2.00, 25, 'dairy'),
(12, 'rice', 2.50, 30, 'grains'),
(13, 'pasta', 1.75, 40, 'grains'),
(14, 'olive oil', 10.00, 10, 'condiments'),
(15, 'vinegar', 3.00, 15, 'condiments'),
(16, 'salt', 0.25, 100, 'condiments'),
(17, 'pepper', 1.00, 50, 'condiments'),
(18, 'sugar', 0.80, 60, 'condiments'),
(19, 'coffee', 5.00, 20, 'beverages'),
(20, 'tea', 3.00, 30, 'beverages'),
(21, 'cookies', 2.50, 23, 'snacks'),
(22, 'chips', 1.50, 40, 'snacks'),
(23, 'candy', 0.75, 100, 'snacks'),
(24, 'frozen pizza', 8.00, 10, 'frozen food'),
(25, 'ice cream', 4.00, 15, 'frozen food'),
(26, 'frozen vegetables', 3.50, 20, 'frozen food'),
(27, 'cereal', 3.00, 25, 'breakfast'),
(28, 'oatmeal', 2.00, 30, 'breakfast'),
(29, 'granola bars', 1.50, 40, 'snacks'),
(30, 'nuts', 6.00, 20, 'snacks');

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

CREATE TABLE `sales` (
  `id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `seller_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','seller') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`) VALUES
(1, 'admin', 'adminpass', 'admin'),
(2, 'seller1', 'sellerpass', 'seller');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bill_report`
--
ALTER TABLE `bill_report`
  ADD PRIMARY KEY (`bill_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`id`),
  ADD KEY `product_id` (`product_id`),
  ADD KEY `seller_id` (`seller_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bill_report`
--
ALTER TABLE `bill_report`
  MODIFY `bill_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `sales`
--
ALTER TABLE `sales`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `sales`
--
ALTER TABLE `sales`
  ADD CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  ADD CONSTRAINT `sales_ibfk_2` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
