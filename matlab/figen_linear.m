clear;
load('data_linear_ds_error.mat');
scatter(data_size,errors*85,'.b');
grid on;
xlabel('Landmark count in Region','FontSize',15);
ylabel('Median error(km)','FontSize',15);