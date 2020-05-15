#!/bin/sh

#./deploy.sh
REGION='eu-west-1'

delete=false
deploy=true

while getopts "d" arg
do
        # shellcheck disable=SC2220
        case "${arg}" in
        d)
                delete=true
                deploy=false
                ;;
    esac
done


#Start the deployment
echo "##########################################################################"

if(${deploy})
then
    echo 'Creating the identity-pool'
    cfn-create-or-update --stack-name cognito-backend --template-body file://cognito-backend.yaml \
    --capabilities CAPABILITY_NAMED_IAM \
    --region ${REGION} --wait
fi

if(${delete})
then
    echo 'Delete identity-pool'
    aws cloudformation delete-stack --stack-name cognito-backend || continue
    aws cloudformation wait stack-delete-complete --stack-name cognito-backend
fi
