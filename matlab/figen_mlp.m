clc;
clear;
load('data_mlp_p60_regterm.mat');

n = length(center_errors);

median_errors = zeros([1,n]);
big_center_errors = [];
big_median_errors = [];
big_data_size = [];
small_center_errors = [];
count = 1;
for i = 1:n
    median_errors(i) = median(error_dist(region_errors{i,1}));
    if data_size(i) >= 100 
        big_median_errors = cat(2, big_median_errors,median_errors(i));
        big_center_errors = cat(2, big_center_errors,center_errors(i));
        big_data_size = cat(2,big_data_size,data_size(i));
        count = count+1;
    end
    if data_size(i) <= 50
        small_center_errors = cat(2, small_center_errors, center_errors(i));
    end
end;

scatter(data_size,median_errors*8.5,'.b');
grid on;
xlabel('Landmark count in Region','FontSize',15);
ylabel('Median error(km)','FontSize',15);

figure;
cdfplot(median_errors*8.5);
xlabel('Error Distance (km)','FontSize',15);
ylabel('Cumulative Probability','FontSize',15);
