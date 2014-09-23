load('data_main_sepds.mat');
sc = 10.5;
nl = 1.2;

[m,n] = size(city_input);

%[city_rb1,city_err1] = train_rb(city_input,city_target,nl,sc);

%err_type1 = city_err1(:,1);

city_input2 = city_input(:,[1,3:n]);
city_target2 = city_target(:,[1,3:n]);

[city_rb2,city_err2] = train_rb(city_input2,city_target2,nl,sc);

err_type2 = city_rb2(city_input(:,2)) - city_target(:,2);
close all;