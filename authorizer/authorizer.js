import { SSMClient, GetParameterCommand } from "@aws-sdk/client-ssm"

const ssm = new SSMClient()
const ssm_command = new GetParameterCommand(
    {
        Name: "matrixscript_private_endpoint_key",
        WithDecryption: true
    }
)
const access_key = (await ssm.send(ssm_command)).Parameter.Value


export const handler = async (e) => {
    const token = e.headers.authorization

    return {
        isAuthorized: (token === access_key)
    }
}