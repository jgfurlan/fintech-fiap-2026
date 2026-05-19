import boto3
import json
import os
from typing import List
from decimal import Decimal
from app.models.seed import ActionItem

class BedrockInsightService:
    def __init__(self):
        self.region = os.environ.get("AWS_REGION", "us-east-1")
        self.client = boto3.client(
            service_name="bedrock-runtime",
            region_name=self.region
        )
        self.model_id = "anthropic.claude-3-sonnet-20240229-v1:0"

    def get_budget_insight(self, category: str, deviation: Decimal, total_spent: Decimal) -> List[ActionItem]:
        """
        Calls AWS Bedrock to generate actionable budget insights.
        """
        prompt = f"""
        You are a financial advisor AI. A user has exceeded their budget in the '{category}' category.
        
        Details:
        - Category: {category}
        - Total Spent So Far: ${total_spent}
        - Deviation from Budget: ${deviation}
        
        Generate exactly 2 actionable, concrete suggestions for the user to save money and get back on track.
        Each suggestion must include:
        1. The specific action.
        2. Estimated projected savings (as a number).
        3. The reason why this helps.
        
        Return the response ONLY as a JSON list of objects with keys: "action", "projected_savings", "reason".
        """

        body = json.dumps({{
            "anthropic_version": "bedrock-2023-05-31",
            "max_tokens": 500,
            "messages": [
                {{
                    "role": "user",
                    "content": prompt
                }}
            ]
        }})

        try:
            response = self.client.invoke_model(
                body=body,
                modelId=self.model_id,
                accept="application/json",
                contentType="application/json"
            )
            response_body = json.loads(response.get("body").read())
            content = response_body.get("content")[0].get("text")
            
            # Basic parsing of the JSON response from Claude
            # In a production app, we'd add more robust cleaning/validation
            items_data = json.loads(content)
            return [ActionItem(**item) for item in items_data]
            
        except Exception as e:
            print(f"Error calling Bedrock: {e}")
            # Fallback to a default suggestion if Bedrock fails
            return [
                ActionItem(
                    action="Review your non-essential spending.",
                    projected_savings=Decimal("50.00"),
                    reason=f"Bedrock service currently unavailable, but {category} is over budget."
                )
            ]
