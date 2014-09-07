

%close all
clear
%clc

disp('2nd order Volterra model')
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

count=0;
%data2=[];
data2=zeros(rowX,colX*(colX+1)/2);
for i=1:colX
    for j=i:colX
        count=count+1;
        %data2=[data2, X(:,i).*X(:,j)];
        data2(:,count)=X(:,i).*X(:,j);
    end
end

method=0;

if method==0
    data=[X, data2];
else
    data=[ones(rowX,1),X,data2];
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
RMSE=sqrt(MSE);
MAE=sum(abs(err))/rowX;

RMSE_norm=norm(RMSE)
MAE_norm=norm(MAE)



