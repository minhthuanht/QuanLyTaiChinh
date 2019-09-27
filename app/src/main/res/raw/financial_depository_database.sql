create TABLE IF NOT EXISTS tbl_wallets(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    balance REAL NOT NULL,
    currency TEXT NOT NULL,
    userId TEXT);

create TABLE IF NOT EXISTS tbl_categories(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    type INTEGER NOT NULL,
    category TEXT NOT NULL,
    icon TEXT NOT NULL,
    parentId INTEGER NOT NULL);

insert into tbl_categories values(1,1,'Ăn uống','icon_116.png',1);
insert into tbl_categories values(2,1,'Cà phê','icon_15.png',1);
insert into tbl_categories values(3,1,'Ăn chung','icon_2.png',1);
insert into tbl_categories values(4,1,'Nhà hàng','icon_133.png',1);
insert into tbl_categories values(5,1,'Hóa đơn & Tiện ích','icon_135.png',5);
insert into tbl_categories values(6,1,'Điện thoại','icon_134.png',5);
insert into tbl_categories values(7,1,'Nước','icon_124.png',5);
insert into tbl_categories values(8,1,'Điện','icon_125.png',5);
insert into tbl_categories values(9,1,'Gas','icon_139.png',5);
insert into tbl_categories values(10,1,'Internet','icon_126.png',5);
insert into tbl_categories values(11,1,'TV','icon_84.png',5);
insert into tbl_categories values(12,1,'Thuê nhà','icon_136.png',5);
insert into tbl_categories values(13,1,'Di chuyển','icon_12.png',13);
insert into tbl_categories values(14,1,'Tắc xi','icon_127.png',13);
insert into tbl_categories values(15,1,'Gửi xe','icon_128.png',13);
insert into tbl_categories values(16,1,'Xăng dầu','icon_129.png',13);
insert into tbl_categories values(17,1,'Bảo dưỡng','icon_130.png',13);
insert into tbl_categories values(18,1,'Mua sắm','icon_7.png',18);
insert into tbl_categories values(19,1,'Quần áo','icon_17.png',18);
insert into tbl_categories values(20,1,'Giày dép','icon_131.png',18);
insert into tbl_categories values(21,1,'Phụ kiện','icon_63.png',18);
insert into tbl_categories values(22,1,'Thiết bị điện tử','icon_9.png',18);
insert into tbl_categories values(23,1,'Bạn bè & Người yêu','icon_1.png',23);
insert into tbl_categories values(24,1,'Giải trí','icon_49.png',24);
insert into tbl_categories values(25,1,'Phim ảnh','icon_6.png',24);
insert into tbl_categories values(26,1,'Trò chơi','icon_33.png',24);
insert into tbl_categories values(27,1,'Du lịch','icon_122.png',27);
insert into tbl_categories values(28,1,'Sức khỏe','icon_58.png',28);
insert into tbl_categories values(29,1,'Thể thao','icon_70.png',28);
insert into tbl_categories values(30,1,'Khám chữa bệnh','icon_143',28);
insert into tbl_categories values(31,1,'Thuốc','icon_142.png',28);
insert into tbl_categories values(32,1,'Chăm sóc cá nhân','icon_132.png',28);
insert into tbl_categories values(33,1,'Quà tặng & Quyên góp','icon_144.png',33);
insert into tbl_categories values(34,1,'Cưới hỏi','icon_10.png',33);
insert into tbl_categories values(35,1,'Tang lễ','icon_11.png',33);
insert into tbl_categories values(36,1,'Từ thiện','icon_117.png',33);
insert into tbl_categories values(37,1,'Gia đình','icon_8.png',37);
insert into tbl_categories values(38,1,'Con cái','icon_38.png',37);
insert into tbl_categories values(39,1,'Sửa chữa nhà cửa','icon_115.png',37);
insert into tbl_categories values(40,1,'Dịch vụ gia đinh','icon_54.png',37);
insert into tbl_categories values(41,1,'Vật nuôi','icon_53.png',37);
insert into tbl_categories values(42,1,'Giáo dục','icon_113.png',42);
insert into tbl_categories values(43,1,'Sách','icon_35.png',42);
insert into tbl_categories values(44,1,'Đầu tư','icon_119.png',49);
insert into tbl_categories values(45,1,'Kinh doanh','icon_59.png',49);
insert into tbl_categories values(46,1,'Bảo hiểm','icon_137.png',49);
insert into tbl_categories values(47,1,'Chi phí','icon_138.png',49);
insert into tbl_categories values(48,1,'Rút tiền','icon_145.png',49);
insert into tbl_categories values(49,1,'Khoản chi khác','icon_22.png',49);
insert into tbl_categories values(50,1,'Mua đồ chung','icon_7.png',49);
insert into tbl_categories values(51,2,'Thưởng','icon_111.png',51);
insert into tbl_categories values(52,2,'Tiền lãi','icon_118.png',56);
insert into tbl_categories values(53,2,'Lương','icon_75.png',53);
insert into tbl_categories values(54,2,'Được tặng','icon_117.png',56);
insert into tbl_categories values(55,2,'Bán đồ','icon_121.png',56);
insert into tbl_categories values(56,2,'Khoản thu khác','icon_23.png',56);
insert into tbl_categories values(57,3,'Cho vay','icon_120.png',49);
insert into tbl_categories values(58,3,'Trả nợ','icon_141.png',49);
insert into tbl_categories values(59,4,'Đi vay','icon_112.png',56);
insert into tbl_categories values(60,4,'Thu nợ','icon_140.png',56);

create TABLE IF NOT EXISTS tbl_transactions(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    currency TEXT NOT NULL,
    trading REAL NOT NULL,
    transaction_date INTEGER NOT NULL,
    note TEXT,
    categoryId INTEGER NOT NULL,
    walletId INTEGER NOT NULL,
    CONSTRAINT fk_categories_id
    FOREIGN KEY (categoryId)
    REFERENCES tbl_categories(_id)
    ON update CASCADE ON delete CASCADE ,
    CONSTRAINT fk_transaction_wallet_id
    FOREIGN KEY (walletId)
    REFERENCES tbl_wallets(_id)
    ON update CASCADE ON delete CASCADE);