%close all
clear
%clc

disp('OLS model')
load ('pcr.mat');
load ('local_testset.mat');

%X=train_input;
%Y=train_target;
X=x';
Y=t';

[rowX,colX] = size(X);
[rowY,colY] = size(Y);

YXcorr=corr(Y,X);
Xcorr=corr(X);

method=0;

if method==0
    data=X;
else
    data=[ones(rowX,1),X];
end

b=(data'*data)\data'*Y;

Yp=data*b;

figure, plot(Y(:,1),Y(:,1),'-b',Y(:,1),Yp(:,1),'.r')
xlabel('Actual'), ylabel('Predicted')
title('Y1')
legend('Actual', 'Predicted')

figure, plot(Y(:,2),Y(:,2),'-b',Y(:,2),Yp(:,2),'.r')
xlabel('Actual'), ylabel('Predicted')
title('Y2')
legend('Actual', 'Predicted')

err=Y-Yp;

figure, plot(err)
xlabel(''), ylabel('Error (ms)^2')
title('Erro')

MSE=sum(err.^2)/rowX;
MAE=sum(abs(err))/rowX;
RMSE=sqrt(MSE);

RMSE_norm=norm(RMSE)
MAE_norm=norm(MAE)



